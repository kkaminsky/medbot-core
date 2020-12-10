package com.kkaminsky.medbotcore.entity

import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value= AccessType.FIELD)
data class Receipt(

        @field:ManyToOne
        @field:JoinColumn(name = "drug_id", nullable = false)
        var drug: Drug,

        @field:ManyToOne
        @field:JoinColumn(name = "treatment_id", nullable = false)
        var treatment: Treatment,

        @field:Column
        @Enumerated(EnumType.STRING)
        var whenEnum: WhenEnum,

        @field:Column
        @Enumerated(EnumType.STRING)
        var periodEnum: PeriodEnum,

        @field:Column
        var countInPeriod: Int,

        @field:Column
        var isActual: Boolean = true

) : BaseEntity<UUID>(UUID.randomUUID())


