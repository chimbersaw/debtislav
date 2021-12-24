package com.chimber.debtislav.service

import com.chimber.debtislav.dto.RegistrationRequest
import com.chimber.debtislav.exception.EmailNotValidException
import com.chimber.debtislav.util.EmailValidator
import org.springframework.stereotype.Service


@Service
class RegistrationService(
    private val userService: UserService,
    private val emailValidator: EmailValidator
) {
    fun register(registrationRequest: RegistrationRequest) {
        val isValid = emailValidator.validate(registrationRequest.email)
        if (!isValid) {
            throw EmailNotValidException()
        }
        userService.registerUser(registrationRequest)
    }
}
