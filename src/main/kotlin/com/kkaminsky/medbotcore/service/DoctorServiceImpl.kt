package com.kkaminsky.medbotcore.service

import com.kkaminsky.medbotcore.controller.DoctorController
import com.kkaminsky.medbotcore.controller.PatientController
import com.kkaminsky.medbotcore.dto.UserPlatformInfoDto
import com.kkaminsky.medbotcore.entity.*
import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo
import com.kkaminsky.medbotcore.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneOffset
import java.util.*

@Service
class DoctorServiceImpl(
        private val treatmentRepository: TreatmentRepository,
        private val patientRepository: PatientRepository,
        private val userMainRepository: UserMainRepository,
        private val doctorRepository: DoctorRepository,
        private val drugRepository: DrugRepository,
        private val receiptRepository: ReceiptRepository,
        private val userPlatformRepository: UserPlatformRepository
) : DoctorService {

    @Transactional
    override fun createTreatment(patientId: UUID, keycloakUserInfo: KeycloakUserInfo): UUID {
        val patient = patientRepository.findPatientById(patientId) ?: throw Exception("not found")
        val oldUser = userMainRepository.findByKeycloakId(keycloakUserInfo.id)
        val doctor = if (oldUser == null) {
            val newUser = UserMain(keycloakUserInfo.id, keycloakUserInfo.username, UserRegisterType.REGISTERED)
            userMainRepository.save(newUser)
            doctorRepository.save(Doctor(newUser))
        } else {
            doctorRepository.findByUserMain(oldUser)?: doctorRepository.save(Doctor(oldUser))
        }
        return treatmentRepository.save(Treatment(
                patient = patient,
                doctor = doctor
        )).id
    }

    @Transactional
    override fun createReceipt(receiptInfo: DoctorController.CreateReceiptDto, keycloakUserInfo: KeycloakUserInfo): UUID {
        val treatment = treatmentRepository.findTreatmentById(receiptInfo.treatmentId)?: throw Exception("fff for treat")
        val drug = drugRepository.findDrugById(receiptInfo.drugId)?: throw Exception("ff for drug")
        val receipt = Receipt(
                drug = drug,
                treatment = treatment,
                whenEnum = receiptInfo.whenEnum,
                periodEnum = receiptInfo.periodEnum,
                countInPeriod = receiptInfo.countInPeriod
        )
        return receiptRepository.save(receipt).id
    }

    @Transactional(readOnly = true)
    override fun getPatientTreatments(patientId: UUID): List<DoctorController.TreatmentDto> {
        return treatmentRepository.findTreatmentsByPatientId(patientId = patientId).map {
            DoctorController.TreatmentDto(
                    id = it.id,
                    dateBegin = it.dateBegin,
                    dateFinish = it.dateFinish,
                    receipts = it.receipts.map {
                        DoctorController.ReceiptInfoDto(
                                drugId = it.drug.id,
                                drugName = it.drug.name,
                                treatmentId = it.treatment.id,
                                countInPeriod = it.countInPeriod,
                                periodEnum = it.periodEnum,
                                whenEnum = it.whenEnum
                        )
                    }
            )
        }
    }

    @Transactional(readOnly = true)
    override fun getAllPatients(): List<PatientController.PatientDto> {
        return patientRepository.findAll().map { patient ->
            PatientController.PatientDto(
                    id =  patient.id,
                    username =  patient.userMain.username,
                    platform = PlatformType.TELEGRAM,
                    platformId = userPlatformRepository.findByUserMain( patient.userMain)!!.externalId!!,
                    breakfastTime = "${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).hour+5)}:${ String.format("%02d",(patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute))}",
                    lanchTime = "${String.format("%02d",patient.lanchTime!!.atZone(ZoneOffset.UTC).hour+5)}: ${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}",
                    dinnerTime = "${String.format("%02d",patient.dinnerTime!!.atZone(ZoneOffset.UTC).hour+5)}:${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}",
                    goToBedTime = "${String.format("%02d",patient.goToBedTime!!.atZone(ZoneOffset.UTC).hour+5)}:${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}",
                    getUpTime = "${String.format("%02d",patient.getUpTime!!.atZone(ZoneOffset.UTC).hour+5)}:${String.format("%02d",patient.breakfastTime!!.atZone(ZoneOffset.UTC).minute)}"
            )
        }
    }

    @Transactional(readOnly = true)
    override fun getAllDrugs(): List<DoctorController.DrugDto> {
        return drugRepository.findAll().map {
            DoctorController.DrugDto(
                    id = it.id,
                    name = it.name,
                    code = it.code
            )
        }
    }

}