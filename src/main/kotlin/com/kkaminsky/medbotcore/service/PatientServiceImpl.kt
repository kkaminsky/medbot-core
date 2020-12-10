package com.kkaminsky.medbotcore.service

import com.kkaminsky.medbotcore.controller.PatientController
import org.apache.commons.lang3.RandomStringUtils
import com.kkaminsky.medbotcore.dto.UserPlatformInfoDto
import com.kkaminsky.medbotcore.dto.ampq.SyncAccountDto
import com.kkaminsky.medbotcore.entity.*
import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo
import com.kkaminsky.medbotcore.repository.PatientRepository
import com.kkaminsky.medbotcore.repository.SyncCodeRepository
import com.kkaminsky.medbotcore.repository.UserMainRepository
import com.kkaminsky.medbotcore.repository.UserPlatformRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@Service
class PatientServiceImpl(
        private val syncCodeRepository: SyncCodeRepository,
        private val patientRepository: PatientRepository,
        private val userMainRepository: UserMainRepository,
        private val userPlatformRepository: UserPlatformRepository,
        private val rabbitTemplate: RabbitTemplate
) : PatientService {

    @Transactional
    override fun generateSyncCode(userPlatform: UserPlatformInfoDto): String {
        val platformId = userPlatform.id
                ?: throw Exception("platformId can't be empty")
        val syncCode = syncCodeRepository.findSyncCodeByPlatformId(platformId)
        return if (syncCode != null) {
            syncCode.code
        } else {
            val newCode = RandomStringUtils.randomAlphabetic(10)
            val newSyncCode = SyncCode(platformId, newCode, userPlatform.platform)
            syncCodeRepository.save(newSyncCode)
            newSyncCode.code
        }
    }

    @Transactional
    override fun callBindPlatform(code: String, keycloakUser: KeycloakUserInfo): Boolean {
        getUserMainByKeycloak(keycloakUser)
        val codeInfo = syncCodeRepository.findSyncCodeByCode(code)
        val userMain = userMainRepository.findByKeycloakId(keycloakId = keycloakUser.id)?:throw Exception("not found")
        return if (codeInfo != null) {
            rabbitTemplate.convertAndSend("medbot-account-sync", SyncAccountDto(keycloakUser.username, codeInfo.platformId, codeInfo.platform, userMain.id))
            syncCodeRepository.delete(codeInfo)
            true
        } else {
            false
        }
    }

    @Transactional
    override fun getUserMainByKeycloak(keycloakUser: KeycloakUserInfo): UserMain {
        val oldUser = userMainRepository.findByKeycloakId(keycloakUser.id)
        return if (oldUser == null) {
            val newUser = UserMain(keycloakUser.id, keycloakUser.username, UserRegisterType.REGISTERED)
            userMainRepository.save(newUser)
            newUser
        } else {
            oldUser
        }
    }

    @Transactional
    override fun getPatientInfoByKeycloak(keycloakUser: KeycloakUserInfo): PatientController.PatientDto {
        //val patient = patientRepository.findPatientByUserMainKeycloakId(keycloakId = keycloakUser.id)?:throw Exception("not found")
        val oldUser = userMainRepository.findByKeycloakId(keycloakUser.id)
        val patient = if (oldUser == null) {
            val newUser = UserMain(keycloakUser.id, keycloakUser.username, UserRegisterType.REGISTERED)
            userMainRepository.save(newUser)
            patientRepository.save(Patient(newUser, Instant.now(), Instant.now(), Instant.now(), Instant.now(), Instant.now()))
        } else {
            patientRepository.findPatientByUserMain(oldUser)?: patientRepository.save(Patient(oldUser, Instant.now(), Instant.now(), Instant.now(), Instant.now(), Instant.now()))
        }
        val platform = userPlatformRepository.findByUserMain(patient.userMain)?:throw Exception("paltform not found")
        return PatientController.PatientDto(
                id = patient.id,
                username = patient.userMain.username,
                platform = PlatformType.TELEGRAM,
                platformId = platform.externalId?:"",
                breakfastTime = "${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).hour+5)}:${ String.format("%02d",(patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute))}",
                lanchTime = "${String.format("%02d",patient.lanchTime!!.atZone(ZoneOffset.UTC).hour+5)}: ${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}",
                dinnerTime = "${String.format("%02d",patient.dinnerTime!!.atZone(ZoneOffset.UTC).hour+5)}:${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}",
                goToBedTime = "${String.format("%02d",patient.goToBedTime!!.atZone(ZoneOffset.UTC).hour+5)}:${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}",
                getUpTime = "${String.format("%02d",patient.getUpTime!!.atZone(ZoneOffset.UTC).hour+5)}:${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}"
        )
    }

    @Transactional
    override fun savePatientInfo(dtoChange: PatientController.ChangePatientDto, keycloakUser: KeycloakUserInfo) {
        val user = userMainRepository.findByKeycloakId(keycloakUser.id)?:throw Exception("User no found")
        val patient = Patient(
                userMain = user,
                breakfastTime = dtoChange.breakfastTime,
                dinnerTime = dtoChange.dinnerTime,
                lanchTime = dtoChange.lanchTime,
                getUpTime = dtoChange.getUpTime,
                goToBedTime = dtoChange.goToBedTime
        )
        patientRepository.save(patient)
    }

    @Transactional
    override fun bindPlatform(syncDto: SyncAccountDto): UUID {
        val userMain = userMainRepository.findUserMainById(syncDto.userMainId)?: throw Exception("not f")
        return userPlatformRepository.save(UserPlatform(
                externalId = syncDto.platformId,
                username = syncDto.username,
                userMain = userMain,
                platform = PlatformType.TELEGRAM
        )).id
    }
}