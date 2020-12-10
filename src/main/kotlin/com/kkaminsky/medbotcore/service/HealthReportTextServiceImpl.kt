package com.kkaminsky.medbotcore.service

import com.kkaminsky.medbotcore.entity.HealthReportText
import com.kkaminsky.medbotcore.repository.HealthReportTextRepository
import com.kkaminsky.medbotcore.repository.TreatmentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class HealthReportTextServiceImpl(
        private val healthReportTextRepository: HealthReportTextRepository,
        private val treatmentRepository: TreatmentRepository
) : HealthReportTextService {
    override fun createHealthReportText(text: String): UUID {
        val treatment = treatmentRepository.findTreatmentByIsFinished() ?:throw Exception("not found")
        return healthReportTextRepository.save(HealthReportText(
                treatment = treatment,
                text = text
        )).id
    }

    override fun findByTreatment(treatmentId: UUID): List<HealthReportTextViewDto> {
        return healthReportTextRepository.findHealthReportTextByTreatmentId(treatmentId).map {
            HealthReportTextViewDto(
                    text = it.text,
                    reportDate = it.reportDate
            )
        }
    }
}