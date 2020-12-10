package com.kkaminsky.medbotcore.keycloak.service

import com.kkaminsky.medbotcore.keycloak.dto.KeycloakUserInfo
import org.keycloak.KeycloakPrincipal
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.context.WebApplicationContext

import java.util.stream.Collectors

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST,proxyMode = ScopedProxyMode.TARGET_CLASS)
class KeycloakUserServiceImpl : KeycloakUserService {

    override fun getCurrentInfo(): KeycloakUserInfo {
            val principal = SecurityContextHolder.getContext().authentication.principal as KeycloakPrincipal<*>
            val userId = principal.keycloakSecurityContext.token.subject
            val email = principal.keycloakSecurityContext.token.email
            val username = principal.keycloakSecurityContext.token.preferredUsername
            val name = principal.keycloakSecurityContext.token.name
            val authorities =  SecurityContextHolder.getContext()
                    .authentication
                    .authorities
                    .stream()
                    .map { it.authority }
                    .collect(Collectors.toList())
            return KeycloakUserInfo(userId,username,email,name,authorities)
    }
}