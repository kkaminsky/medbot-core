package com.kkaminsky.medbotcore.keycloak.service

import org.keycloak.admin.client.Keycloak
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import com.kkaminsky.medbotcore.keycloak.config.KeycloakPropertyReader
import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo

@Service
class KeycloakAdminServiceImpl constructor(
    private val keycloakAdminClient: Keycloak,
    private val keycloakPropertyReader: KeycloakPropertyReader
) : KeycloakAdminService {
    private val log = LoggerFactory.getLogger(KeycloakAdminService::class.java)

    override fun getUserInfoById(keycloakId: String): KeycloakUserInfo {
        val user = keycloakAdminClient
            .realm(keycloakPropertyReader.getProperty("keycloak.realm"))
            .users()
            .get(keycloakId)
            .toRepresentation()

        val fullname =
            if (user.firstName != null && user.lastName != null) "${user.firstName} ${user.lastName}" else null
        return KeycloakUserInfo(user.id, user.username, user.email, fullname, listOf())
    }

}