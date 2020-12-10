package com.kkaminsky.medbotcore.keycloak.config

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KeycloakAdminClientConfig {
    @Bean
    fun buildKeycloakAdminClient(keycloakPropertyReader: KeycloakPropertyReader): Keycloak {
          return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.PASSWORD)
                .serverUrl(keycloakPropertyReader.getProperty("keycloak.auth-server-url"))
                .realm(keycloakPropertyReader.getProperty("keycloak.realm"))
                .clientSecret(keycloakPropertyReader.getProperty("keycloak.credentials.secret"))
                .clientId(keycloakPropertyReader.getProperty("keycloak.resource"))
                .username(keycloakPropertyReader.getProperty("keycloak-admin.username"))
                .password(keycloakPropertyReader.getProperty("keycloak-admin.password"))
                .build()
    }

}