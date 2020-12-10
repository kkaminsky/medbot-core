package com.kkaminsky.medbotcore.entity

import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value= AccessType.FIELD)
data class UserPlatform(

        @field:Column
        var externalId: String?,

        @field:Column
        var username: String?,

        @field:ManyToOne
        @field:JoinColumn(name = "user_main_id", nullable = false)
        var userMain: UserMain,

        @field:Column
        @Enumerated(EnumType.STRING)
        var platform: PlatformType



): BaseEntity<UUID>(UUID.randomUUID()){

    override fun toString(): String {
        return "UserPlatform(username=$username, platformExternalId=$externalId, platform=${platform.name}, ${super.toString()})"
    }
}

