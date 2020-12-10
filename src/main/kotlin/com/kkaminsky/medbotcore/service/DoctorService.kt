package com.kkaminsky.medbotcore.service

import com.kkaminsky.medbotcore.controller.DoctorController
import com.kkaminsky.medbotcore.controller.PatientController
import com.kkaminsky.medbotcore.dto.UserPlatformInfoDto
import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo
import java.util.*

interface DoctorService {

    fun createTreatment(patientId: UUID,keycloakUserInfo: KeycloakUserInfo) :UUID
    fun createReceipt(receiptInfo: DoctorController.CreateReceiptDto,keycloakUserInfo: KeycloakUserInfo): UUID
    fun getPatientTreatments(patientId: UUID): List<DoctorController.TreatmentDto>
    fun getAllPatients(): List<PatientController.PatientDto>
    fun getAllDrugs(): List<DoctorController.DrugDto>

}