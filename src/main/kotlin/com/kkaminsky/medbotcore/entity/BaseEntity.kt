package com.kkaminsky.medbotcore.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp


import org.springframework.data.domain.Persistable
import java.time.Instant
import javax.persistence.*

@MappedSuperclass
@Access(value = AccessType.FIELD)
abstract class BaseEntity<T>(
        @Id private val id: T
) : Persistable<T> {

    @field:CreationTimestamp
    var createTime: Instant? = null

    @field:UpdateTimestamp
    var updateTime: Instant? = null

    override fun getId(): T {
        return id
    }

    override fun isNew(): Boolean {
        return id == null
    }

    override fun toString(): String {
        return "BaseEntity(id=$id, createTime=$createTime, updateTime=$updateTime, isNew=$isNew)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BaseEntity<*>
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}