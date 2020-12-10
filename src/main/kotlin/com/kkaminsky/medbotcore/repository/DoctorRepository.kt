package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.Doctor
import com.kkaminsky.medbotcore.entity.Patient
import com.kkaminsky.medbotcore.entity.UserMain
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DoctorRepository : JpaRepository<Doctor, UUID> {
    fun findByUserMain(userMain: UserMain): Doctor?
}