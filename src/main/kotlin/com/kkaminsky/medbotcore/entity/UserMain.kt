package com.kkaminsky.medbotcore.entity

import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value = AccessType.FIELD)
data class UserMain(

        @field:Column
        val keycloakId: String?,

        @field:Column
        val username: String?,

        @field:Column
        @Enumerated(EnumType.STRING)
        var registration: UserRegisterType


) : BaseEntity<UUID>(UUID.randomUUID()) {

    override fun toString(): String {
        return "UserMain(keycloakId=$keycloakId, username=$username, registration=${registration.name}, ${super.toString()})"
    }
}


