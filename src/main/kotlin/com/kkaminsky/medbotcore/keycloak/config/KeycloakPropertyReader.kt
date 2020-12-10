package com.kkaminsky.medbotcore.keycloak.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment


@Configuration
@PropertySource("classpath:application.yml")
class KeycloakPropertyReader {
    @Autowired
    private lateinit var env: Environment

    fun getProperty(key: String): String {
        return env.getProperty(key)!!
    }
}