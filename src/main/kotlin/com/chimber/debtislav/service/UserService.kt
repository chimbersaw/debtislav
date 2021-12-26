package com.chimber.debtislav.service

import com.chimber.debtislav.dto.RegistrationRequest
import com.chimber.debtislav.exception.UserNotFoundException
import com.chimber.debtislav.model.User
import com.chimber.debtislav.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(registrationRequest: RegistrationRequest) {
        val emailExists = userRepository.findByEmail(registrationRequest.email).isPresent
        if (emailExists) {
            throw IllegalStateException("User with this email already exists")
        }
        val usernameExists = userRepository.findByUsername(registrationRequest.username).isPresent
        if (usernameExists) {
            throw IllegalStateException("User with this name already exists")
        }
        val encodedPassword: String = passwordEncoder.encode(registrationRequest.password)
        val user = User(
            email = registrationRequest.email,
            username = registrationRequest.username,
            password = encodedPassword,
        )
        userRepository.save(user)
    }

    fun getUsername(username: String): String {
        return userRepository.findByUsername(username).orElseThrow {
            UserNotFoundException("No user with name $username exists")
        }.username
    }
}
