package com.kkaminsky.medbotcore.controller

import com.kkaminsky.medbotcore.entity.PeriodEnum
import com.kkaminsky.medbotcore.entity.WhenEnum
import com.kkaminsky.medbotcore.keycloak.service.KeycloakUserService
import com.kkaminsky.medbotcore.service.DoctorService
import com.kkaminsky.medbotcore.service.HealthReportService
import com.kkaminsky.medbotcore.service.HealthReportTextService
import com.kkaminsky.medbotcore.service.HealthReportTextViewDto
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/v1/doctor")
class DoctorController(
        private val keycloakUserService: KeycloakUserService,
        private val doctorService: DoctorService,
        private val healthReportService: HealthReportService,
        private val healthReportTextService: HealthReportTextService
) {

    @PostMapping("/create/treatment")
    fun createTreatment(@RequestBody body: PatientInfoDto): UUID {
        val keycloakUser = keycloakUserService.getCurrentInfo()
        return doctorService.createTreatment(
                patientId = body.patientId,
                keycloakUserInfo = keycloakUser
        )
    }

    @PostMapping("/create/receipt")
    fun createReceipt(@RequestBody body: CreateReceiptDto): UUID {
        val keycloakUser = keycloakUserService.getCurrentInfo()
        return doctorService.createReceipt(
                receiptInfo = body,
                keycloakUserInfo = keycloakUser
        )
    }

    @PostMapping("/get/treatments")
    fun getTreatments(@RequestBody body: PatientInfoDto): List<TreatmentDto>{
        return doctorService.getPatientTreatments(patientId = body.patientId)
    }

    @GetMapping("/get/patients")
    fun getAllPatients(): List<PatientController.PatientDto>{
        return doctorService.getAllPatients()
    }

    @GetMapping("/get/drugs")
    fun getAllDrugs(): List<DrugDto>{
        return doctorService.getAllDrugs()
    }

    @PostMapping("/create/notifications")
    fun createNotificationsTasks(@RequestBody body: TreatmentIdDto){
        return healthReportService.createReportTasks(body.treatmentId)
    }

    @PostMapping("/get/reports")
    fun getTreatmentReports(@RequestBody body: TreatmentIdDto):List<HealthReportViewDto>{
        return healthReportService.getReportsByTreatment(treatmentId = body.treatmentId)
    }

    @PostMapping("/get/reports/text")
    fun getTextReports(@RequestBody body: TreatmentIdDto): List<HealthReportTextViewDto>{
        return healthReportTextService.findByTreatment(treatmentId = body.treatmentId)
    }


    data class PatientInfoDto(
            val patientId: UUID
    )

    data class TreatmentIdDto(
            val treatmentId: UUID
    )

    data class HealthReportViewDto(
            var isMissed: Boolean = true,
            var reportDate: Instant = Instant.now()
    )

    data class DrugDto(
            val id: UUID,
            val name: String,
            val code: String
    )

    data class ReceiptInfoDto(
            val drugId: UUID,
            val drugName: String,
            val treatmentId: UUID,
            val countInPeriod: Int,
            val periodEnum: PeriodEnum,
            val whenEnum: WhenEnum
    )

    data class CreateReceiptDto(
            val drugId: UUID,
            val treatmentId: UUID,
            val countInPeriod: Int,
            val periodEnum: PeriodEnum,
            val whenEnum: WhenEnum
    )

    data class TreatmentDto(
            val id: UUID,
            val dateBegin: Instant,
            val dateFinish: Instant,
            val receipts: List<ReceiptInfoDto>
    )
}