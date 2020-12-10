package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.Patient
import com.kkaminsky.medbotcore.entity.UserMain
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PatientRepository : JpaRepository<Patient, UUID> {
    fun findPatientById(id: UUID): Patient?
    fun findPatientByUserMain(userMain: UserMain): Patient?
    fun findPatientByUserMainKeycloakId(keycloakId: String): Patient?
}