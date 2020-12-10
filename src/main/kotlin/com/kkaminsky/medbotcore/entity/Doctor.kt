package com.kkaminsky.medbotcore.entity

import java.util.*
import javax.persistence.*

@Entity
@Table
@Access(value = AccessType.FIELD)
data class Doctor(
        @field:OneToOne
        @field:JoinColumn(name = "user_main_id", nullable = false)
        var userMain: UserMain,
): BaseEntity<UUID>(UUID.randomUUID()){
    override fun toString(): String {
        return "Doctor(platformId=${userMain.username})"
    }
}
