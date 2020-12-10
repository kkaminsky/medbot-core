package com.kkaminsky.medbotcore.entity

import java.util.*
import javax.persistence.*


@Entity
@Table
@Access(value= AccessType.FIELD)
data class Drug(

        @field:Column
        var name: String,

        @field:Column
        var code: String


): BaseEntity<UUID>(UUID.randomUUID())
