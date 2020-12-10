package com.kkaminsky.medbotcore.entity

import java.time.Duration
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table
@Access(value= AccessType.FIELD)
data class Treatment(
        @field:ManyToOne
        @field:JoinColumn(name = "patient_id", nullable = false)
        var patient: Patient,

        @field:ManyToOne
        @field:JoinColumn(name = "doctor_id", nullable = false)
        var doctor: Doctor,

        @field:Column
        var isFinished: Boolean  = false,


        @field:Column
        var dateBegin: Instant = Instant.now(),

        @field:Column
        var dateFinish: Instant = Instant.now().plus(Duration.ofDays(7)),

        @OneToMany(mappedBy = "treatment")
        var receipts: List<Receipt> = listOf()

) : BaseEntity<UUID>(UUID.randomUUID())
