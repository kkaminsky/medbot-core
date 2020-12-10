package com.kkaminsky.medbotcore.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.kkaminsky.medbotcore.entity.PlatformType
import com.kkaminsky.medbotcore.keycloak.service.KeycloakUserService
import com.kkaminsky.medbotcore.service.PatientService
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/v1/patient")
class PatientController(
        private val patientService: PatientService,
        private val keycloakUserService: KeycloakUserService
) {

    @PostMapping(value = ["/platform"])
    fun syncUser(@RequestBody body: CodePlatform): Boolean {
        val keycloakUser = keycloakUserService.getCurrentInfo()
        return patientService.callBindPlatform(body.code, keycloakUser)
    }


    @PostMapping(value = ["/save"])
    fun savePatientInfo(@RequestBody body: ChangePatientDto){
        val keycloakUser = keycloakUserService.getCurrentInfo()
        patientService.savePatientInfo(
                dtoChange = body,
                keycloakUser = keycloakUser
        )
    }

    @GetMapping("/get/me")
    fun getCurrnetPatient(): PatientDto{
        val keycloakUser = keycloakUserService.getCurrentInfo()
        return patientService.getPatientInfoByKeycloak(keycloakUser)
    }


    data class CodePlatform(
            @param:JsonProperty("code", required = true) val code: String
    )

    data class PatientDto(
            @param:JsonProperty("id", required = true) val id: UUID,
            @param:JsonProperty("username", required = true) val username: String?,
            @param:JsonProperty("platform", required = true) val platform: PlatformType,
            @param:JsonProperty("platformId", required = true) val platformId: String,
            @param:JsonProperty("breakfastTime", required = false) val breakfastTime: String,
            @param:JsonProperty("lanchTime", required = false) val lanchTime: String ,
            @param:JsonProperty("dinnerTime", required = false) val dinnerTime: String,
            @param:JsonProperty("getUpTime", required = false) val getUpTime: String,
            @param:JsonProperty("goToBedTime", required = false) val goToBedTime: String
    )

    data class ChangePatientDto(
            @param:JsonProperty("breakfastTime", required = false) val breakfastTime: Instant = Instant.now(),
            @param:JsonProperty("lanchTime", required = false) val lanchTime: Instant = Instant.now() ,
            @param:JsonProperty("dinnerTime", required = false) val dinnerTime: Instant = Instant.now(),
            @param:JsonProperty("getUpTime", required = false) val getUpTime: Instant = Instant.now(),
            @param:JsonProperty("goToBedTime", required = false) val goToBedTime: Instant = Instant.now()
    )
}