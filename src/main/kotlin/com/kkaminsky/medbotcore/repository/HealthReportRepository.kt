package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.HealthReport
import com.kkaminsky.medbotcore.entity.Patient
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HealthReportRepository : JpaRepository<HealthReport, UUID> {
    fun findHealthReportsByReceiptTreatmentId(treatmentId: UUID):List<HealthReport>
}