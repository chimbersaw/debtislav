package com.chimber.debtislav.controller;


import com.chimber.debtislav.dto.AddUserToGroupRequest
import com.chimber.debtislav.dto.GroupCreateRequest
import com.chimber.debtislav.dto.GroupIdRequest
import com.chimber.debtislav.exception.NotAuthorizedException
import com.chimber.debtislav.service.GroupService;
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/group")
class GroupController(private val groupService: GroupService) {
    private fun getCurrentUsername() =
        SecurityContextHolder.getContext().authentication?.principal?.toString() ?: throw NotAuthorizedException()

    @PostMapping("/create")
    fun createGroup(@RequestBody request: GroupCreateRequest): Long {
        return groupService.createGroup(getCurrentUsername(), request.name)
    }

    @PostMapping("/add")
    fun addUser(@RequestBody request: AddUserToGroupRequest) {
        groupService.addToGroup(getCurrentUsername(), request.username, request.groupId)
    }

    @GetMapping("/users")
    fun getAllUsers(@RequestBody request: GroupIdRequest): List<String> {
        return groupService.getAllUsers(request.id)
    }
}
