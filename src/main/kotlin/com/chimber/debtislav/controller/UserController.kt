package com.chimber.debtislav.controller

import com.chimber.debtislav.repository.UserRepository
import com.chimber.debtislav.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user")
class UserController(
    userRepository: UserRepository,
    private val userService: UserService
) : AbstractUserController(userRepository) {
    @GetMapping("/groups")
    fun getAllGroups(): List<String> {
        return userService.getAllGroups(currentUser)
    }
}
