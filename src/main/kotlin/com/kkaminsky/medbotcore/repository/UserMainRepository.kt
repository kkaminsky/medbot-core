package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.UserMain
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserMainRepository : JpaRepository<UserMain, UUID> {
    fun findUserMainById(userId: UUID): UserMain?
    fun findByUsername(username: String): UserMain?
    fun findByKeycloakId(keycloakId: String): UserMain?
}
