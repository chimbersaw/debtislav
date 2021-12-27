package com.chimber.debtislav.controller;


import com.chimber.debtislav.dto.AddUserToGroupRequest
import com.chimber.debtislav.dto.GroupCreateRequest
import com.chimber.debtislav.dto.GroupIdRequest
import com.chimber.debtislav.repository.UserRepository
import com.chimber.debtislav.service.GroupService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/group")
class GroupController(
    private val userRepository: UserRepository,
    private val groupService: GroupService
) : AbstractUserController(userRepository) {
    @PostMapping("/create")
    fun createGroup(@RequestBody request: GroupCreateRequest): Long {
        return groupService.createGroup(currentUser, request.name)
    }

    @PostMapping("/add")
    fun addUser(@RequestBody request: AddUserToGroupRequest) {
        groupService.addToGroup(currentUser, userRepository.getUserOrThrow(request.username), request.groupId)
    }

    @GetMapping("/users")
    fun getAllUsers(@RequestBody request: GroupIdRequest): List<String> {
        return groupService.getAllUsers(request.id)
    }

    @GetMapping("/debts")
    fun getAllDebts(@RequestBody request: GroupIdRequest): List<String> {
        return groupService.getAllDebts(request.id)
    }
}
