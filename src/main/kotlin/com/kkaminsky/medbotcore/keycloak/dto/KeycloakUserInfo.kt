package com.kkaminsky.medbotcore.keycloak.dto

data class KeycloakUserInfo(
        val id: String,
        val username: String,
        val email: String?,
        val name: String?,
        val roles : List<String>
)