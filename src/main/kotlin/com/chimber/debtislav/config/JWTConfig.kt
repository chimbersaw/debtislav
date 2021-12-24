package com.chimber.debtislav.config

import org.springframework.context.annotation.Configuration


@Configuration
class JWTConfig {
    val secret: String by lazy {
        System.getenv("JWT_SECRET") ?: "default_JWT_secret"
    }

    val expirationTime: Int by lazy {
        System.getenv("JWT_EXPIRES")?.toInt() ?: 1209600000 // 2 weeks in ms
    }
}
