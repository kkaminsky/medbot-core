package com.kkaminsky.medbotcore.service

import com.kkaminsky.medbotcore.controller.PatientController
import com.kkaminsky.medbotcore.dto.UserPlatformInfoDto
import com.kkaminsky.medbotcore.dto.ampq.SyncAccountDto
import com.kkaminsky.medbotcore.entity.UserMain
import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo
import java.util.*

interface PatientService {

    fun generateSyncCode(userPlatform: UserPlatformInfoDto): String
    fun callBindPlatform(code: String, keycloakUser: KeycloakUserInfo): Boolean
    fun getUserMainByKeycloak(keycloakUser: KeycloakUserInfo): UserMain
    fun getPatientInfoByKeycloak(keycloakUser: KeycloakUserInfo): PatientController.PatientDto
    fun savePatientInfo(dtoChange: PatientController.ChangePatientDto, keycloakUser: KeycloakUserInfo)
    fun bindPlatform(syncDto: SyncAccountDto): UUID
}