package com.kkaminsky.medbotcore.repository


import com.kkaminsky.medbotcore.entity.HealthReportText
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HealthReportTextRepository : JpaRepository<HealthReportText, UUID> {
    fun findHealthReportTextByTreatmentId(treatmentId: UUID): List<HealthReportText>
}