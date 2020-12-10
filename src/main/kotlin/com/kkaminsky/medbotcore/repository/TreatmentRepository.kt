package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.Treatment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TreatmentRepository : JpaRepository<Treatment, UUID> {
    fun findTreatmentById(treatmentId: UUID): Treatment?
    fun findTreatmentsByPatientId(patientId: UUID): List<Treatment>
    fun findTreatmentByIsFinished(isFinished: Boolean = false):Treatment?
}