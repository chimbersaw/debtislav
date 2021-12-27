package com.chimber.debtislav.controller

import com.chimber.debtislav.exception.NotAuthorizedException
import com.chimber.debtislav.model.User
import com.chimber.debtislav.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder

abstract class AbstractUserController(private val userRepository: UserRepository) {
    private val currentUsername: String
        get() = SecurityContextHolder.getContext().authentication?.principal?.toString()
            ?: throw NotAuthorizedException()

    protected val currentUser: User
        get() = userRepository.getUserOrThrow(currentUsername)
}
