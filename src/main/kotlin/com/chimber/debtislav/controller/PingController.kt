package com.chimber.debtislav.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class PingController {
    @GetMapping("/api/ping")
    fun ping() = "ping ok"

    @GetMapping("/health")
    fun health() = "Healthy!"
}
