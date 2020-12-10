package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.Doctor
import com.kkaminsky.medbotcore.entity.Drug
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DrugRepository : JpaRepository<Drug, UUID> {
    fun findDrugById(drugId: UUID): Drug?
}