package com.chimber.debtislav.controller

import com.chimber.debtislav.exception.NotAuthorizedException
import com.chimber.debtislav.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {
    private fun getCurrentUsername() =
        SecurityContextHolder.getContext().authentication?.principal?.toString() ?: throw NotAuthorizedException()

    @GetMapping("/groups")
    fun getAllGroups(): List<String> {
        return userService.getAllGroups(getCurrentUsername())
    }
}
