package com.kkaminsky.medbotcore.dto.ampq

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.kkaminsky.medbotcore.entity.PlatformType
import java.util.*

data class SyncAccountDto @JsonCreator constructor(
        @param:JsonProperty("username", required = true) val username :String,
        @param:JsonProperty("platformId", required = true) val platformId :String,
        @param:JsonProperty("platformType", required = true) val platformType : PlatformType,
        @param:JsonProperty("userMainId", required = true) val userMainId: UUID
)