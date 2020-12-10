package com.kkaminsky.medbotcore.entity

import java.time.Instant
import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value= AccessType.FIELD)
data class HealthReport(
        @field:ManyToOne
        @field:JoinColumn(name = "receipt_id", nullable = false)
        var receipt: Receipt,

        @field:Column
        var isMissed: Boolean = true,

        @field:Column
        var reportDate: Instant = Instant.now()
) : BaseEntity<UUID>(UUID.randomUUID())
