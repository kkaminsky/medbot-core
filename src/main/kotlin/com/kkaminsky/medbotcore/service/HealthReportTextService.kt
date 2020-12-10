package com.kkaminsky.medbotcore.service

import java.time.Instant
import java.util.*

interface HealthReportTextService {
    fun createHealthReportText(text: String): UUID
    fun findByTreatment(treatmentId: UUID): List<HealthReportTextViewDto>
}

data class HealthReportTextViewDto(
        val text: String,
        val reportDate: Instant
)