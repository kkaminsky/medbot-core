package com.kkaminsky.medbotcore.keycloak.service

import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo

interface KeycloakAdminService {
    fun getUserInfoById(keycloakId:String) : KeycloakUserInfo
}