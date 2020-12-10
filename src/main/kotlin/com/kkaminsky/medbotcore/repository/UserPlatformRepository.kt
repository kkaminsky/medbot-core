package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.UserMain
import com.kkaminsky.medbotcore.entity.UserPlatform
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserPlatformRepository: JpaRepository<UserPlatform, UUID> {
    fun findByUserMain(userMain: UserMain): UserPlatform?
}