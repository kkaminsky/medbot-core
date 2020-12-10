package com.kkaminsky.medbotcore.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.kkaminsky.medbotcore.entity.PlatformType
import java.util.*


data class UserPlatformInfoDto @JsonCreator constructor(
        @param:JsonProperty("id", required = true) val id: String?,
        @param:JsonProperty("username", required = true) val username: String?,
        @param:JsonProperty("platform", required = true) val platform: PlatformType
)

data class UserPlatformIdResponse @JsonCreator constructor(
        @param:JsonProperty("userMainId", required = true) val userMainId: UUID,
        @param:JsonProperty("platformId", required = true) val platformId: String?,
        @param:JsonProperty("platformUsername", required = true) val platformUsername: String?
)

data class UserBindPlatformRequest @JsonCreator constructor(
        @param:JsonProperty("platformInfo", required = true) val platformInfo: UserPlatformInfoDto,
        @param:JsonProperty("username", required = true) val username: String
)