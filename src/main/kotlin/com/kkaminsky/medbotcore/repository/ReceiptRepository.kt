package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.Drug
import com.kkaminsky.medbotcore.entity.Receipt
import com.kkaminsky.medbotcore.entity.Treatment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReceiptRepository : JpaRepository<Receipt, UUID> {
    fun findAllByTreatment(treatment: Treatment): List<Receipt>
    fun findReceiptById(id: UUID): Receipt?
}