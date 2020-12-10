package com.kkaminsky.medbotcore.entity

import java.time.Instant
import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value= AccessType.FIELD)
data class Patient(
        @field:OneToOne
        @field:JoinColumn(name = "user_main_id", nullable = false)
        var userMain: UserMain,

        @field:Column
        var breakfastTime: Instant?,

        @field:Column
        var lanchTime: Instant?,

        @field:Column
        var dinnerTime: Instant?,

        @field:Column
        var getUpTime: Instant?,

        @field:Column
        var goToBedTime: Instant?

) : BaseEntity<UUID>(UUID.randomUUID())
