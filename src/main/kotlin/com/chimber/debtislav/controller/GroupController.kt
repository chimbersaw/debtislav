package com.chimber.debtislav.controller;


import com.chimber.debtislav.dto.AddUserToGroupRequest
import com.chimber.debtislav.dto.GroupCreateRequest
import com.chimber.debtislav.dto.GroupIdRequest
import com.chimber.debtislav.model.Debt
import com.chimber.debtislav.repository.GroupRepository
import com.chimber.debtislav.repository.UserRepository
import com.chimber.debtislav.service.GroupService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/group")
class GroupController(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val groupService: GroupService
) : AbstractUserController(userRepository) {
    @PostMapping("/create")
    fun createGroup(@RequestBody request: GroupCreateRequest): Long {
        return groupService.createGroup(currentUser, request.name)
    }

    @PostMapping("/add")
    fun addUser(@RequestBody request: AddUserToGroupRequest) {
        val group = groupRepository.getGroupOrThrow(request.groupId)
        val user = userRepository.getUserOrThrow(request.username)
        groupService.addToGroup(currentUser, user, group)
    }

    @GetMapping("/users")
    fun getAllUsers(@RequestBody request: GroupIdRequest): List<String> {
        return groupService.getAllUsers(groupRepository.getGroupOrThrow(request.id))
    }

    @GetMapping("/debts")
    fun getAllDebts(@RequestBody request: GroupIdRequest): List<Debt> {
        return groupService.getAllDebts(groupRepository.getGroupOrThrow(request.id))
    }

    @PostMapping("/minimize")
    fun minimizeDebts(@RequestBody request: GroupIdRequest) {
        groupService.minimizeDebts(groupRepository.getGroupOrThrow(request.id))
    }

    @PostMapping("/reduce_cycles")
    fun reduceCycles(@RequestBody request: GroupIdRequest) {
        groupService.reduceCycles(groupRepository.getGroupOrThrow(request.id))
    }
}
