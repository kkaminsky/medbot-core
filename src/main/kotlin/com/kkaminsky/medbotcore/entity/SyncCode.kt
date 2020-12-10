package com.kkaminsky.medbotcore.entity


import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value = AccessType.FIELD)
data class SyncCode(

        @field:Column
        var platformId: String,

        @field:Column
        var code: String,

        @field:Column
        @Enumerated(EnumType.STRING)
        var platform: PlatformType

) : BaseEntity<UUID>(UUID.randomUUID()) {

    override fun toString(): String {
        return "SyncCode(platformId=$platformId," +
                " code=$code," +
                " platform=${platform.name}," +
                " ${super.toString()})"
    }
}