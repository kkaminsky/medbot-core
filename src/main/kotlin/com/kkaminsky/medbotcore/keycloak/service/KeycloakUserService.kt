package com.kkaminsky.medbotcore.keycloak.service

import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo


interface KeycloakUserService {
    fun getCurrentInfo(): KeycloakUserInfo
}