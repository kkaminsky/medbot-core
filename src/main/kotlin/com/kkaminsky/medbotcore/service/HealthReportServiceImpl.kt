package com.kkaminsky.medbotcore.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.kkaminsky.medbotcore.controller.DoctorController
import com.kkaminsky.medbotcore.entity.HealthReport
import com.kkaminsky.medbotcore.entity.PeriodEnum
import com.kkaminsky.medbotcore.entity.WhenEnum
import com.kkaminsky.medbotcore.repository.HealthReportRepository
import com.kkaminsky.medbotcore.repository.ReceiptRepository
import com.kkaminsky.medbotcore.repository.TreatmentRepository
import com.kkaminsky.medbotcore.repository.UserPlatformRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneOffset
import java.util.*

@Service
class HealthReportServiceImpl(
        private val threadPoolTaskScheduler: ThreadPoolTaskScheduler,
        private val treatmentRepository: TreatmentRepository,
        private val rabbitTemplate: RabbitTemplate,
        private val receiptRepository: ReceiptRepository,
        private val userPlatformRepository: UserPlatformRepository,
        private val healthReportRepository: HealthReportRepository
) : HealthReportService {
    override fun createReportTasks(treatmentId: UUID) {
        val treatment = treatmentRepository.findTreatmentById(treatmentId) ?: throw Exception("f for treat")
        val userPlatform = userPlatformRepository.findByUserMain(treatment.patient.userMain) ?: throw Exception("F")
        val receipts = receiptRepository.findAllByTreatment(treatment)
        receipts.forEach { receipt ->
            threadPoolTaskScheduler.schedule(
                    Runnable {
                        println("run")
                        rabbitTemplate.convertAndSend("medbot-report", HealthReportDto(
                                drugName = receipt.drug.name,
                                userPlatformId = userPlatform.externalId ?: throw Exception("Exp"),
                                receiptId = receipt.id
                        ))
                    },
                    CronTrigger(
                            if (receipt.periodEnum == PeriodEnum.DAY) {
                                if (receipt.whenEnum == WhenEnum.BEFORE_SLEEPING) {
                                    "0 " + treatment.patient.goToBedTime!!.atZone(ZoneOffset.UTC).minute + " " + (treatment.patient.goToBedTime!!.atZone(ZoneOffset.UTC).hour + 5) + " * * *"
                                } else {
                                    ""
                                }
                            } else {
                                ""
                            }
                    )
            )
        }

    }

    @Transactional
    override fun createReport(receiptId: UUID): UUID {
        val receipt = receiptRepository.findReceiptById(receiptId)?: throw Exception("F")
        val report = HealthReport(
                receipt = receipt
        )
        return healthReportRepository.save(report).id
    }

    @Transactional(readOnly = true)
    override fun getReportsByTreatment(treatmentId: UUID): List<DoctorController.HealthReportViewDto> {
        return healthReportRepository.findHealthReportsByReceiptTreatmentId(treatmentId).map {
            DoctorController.HealthReportViewDto(
                    isMissed = it.isMissed,
                    reportDate = it.reportDate
            )
        }
    }

    data class HealthReportDto  @JsonCreator constructor (
            @param:JsonProperty("userPlatformId", required = true) val userPlatformId: String,
            @param:JsonProperty("drugName", required = true) val drugName: String,
            @param:JsonProperty("receiptId", required = true) val receiptId: UUID
    )
}