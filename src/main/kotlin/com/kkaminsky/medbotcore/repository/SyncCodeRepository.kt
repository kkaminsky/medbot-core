package com.kkaminsky.medbotcore.repository

import com.kkaminsky.medbotcore.entity.SyncCode
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SyncCodeRepository : JpaRepository<SyncCode, UUID> {
    fun findSyncCodeById(id:UUID):SyncCode?
    fun findSyncCodeByPlatformId(platformId:String):SyncCode?
    fun findSyncCodeByCode(code:String):SyncCode?
}
