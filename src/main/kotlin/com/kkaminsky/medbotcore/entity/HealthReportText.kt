package com.kkaminsky.medbotcore.entity

import java.time.Instant
import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value= AccessType.FIELD)
data class HealthReportText(

        @field:ManyToOne
        @field:JoinColumn(name = "treatment_id", nullable = false)
        var treatment: Treatment,


        @field:Column
        var text: String,

        @field:Column
        var reportDate: Instant = Instant.now()
) : BaseEntity<UUID>(UUID.randomUUID())
