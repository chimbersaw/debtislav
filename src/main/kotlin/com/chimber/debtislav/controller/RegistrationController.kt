package com.chimber.debtislav.controller

import com.chimber.debtislav.dto.RegistrationRequest
import com.chimber.debtislav.service.RegistrationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/registration")
class RegistrationController(private val registrationService: RegistrationService) {
    @PostMapping
    fun register(@RequestBody request: RegistrationRequest): ResponseEntity<String> {
        return try {
            registrationService.register(request)
            ResponseEntity("Registration successful", HttpStatus.OK)
        } catch (e: IllegalStateException) {
            ResponseEntity("Registration failed: ${e.message}", HttpStatus.CONFLICT)
        }
    }
}
