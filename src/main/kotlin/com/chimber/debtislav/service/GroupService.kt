package com.chimber.debtislav.service

import com.chimber.debtislav.exception.NotAuthorizedException
import com.chimber.debtislav.model.Group
import com.chimber.debtislav.repository.GroupRepository
import com.chimber.debtislav.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    fun addToGroup(currentName: String, username: String, groupId: Long) {
        val current = userRepository.getUserOrThrow(currentName)
        val group = groupRepository.getGroupOrThrow(groupId)
        if (current.id != group.admin_id) throw NotAuthorizedException()

        val user = userRepository.getUserOrThrow(username)
        group.userList.add(user)
        groupRepository.save(group)
    }

    fun createGroup(username: String, groupName: String): Long {
        val user = userRepository.getUserOrThrow(username)
        val group = Group(
            name = groupName,
            admin_id = user.id
        )
        group.userList.add(user)
        groupRepository.save(group)
        return group.id
    }

    fun getAllUsers(groupId: Long): List<String> {
        val group = groupRepository.getGroupOrThrow(groupId)
        return group.userList.map { it.toString() }.toList()
    }

    fun getAllDebts(groupId: Long): List<String> {
        val group = groupRepository.getGroupOrThrow(groupId)
        return group.debtList.map { it.toString() }.toList()
    }
}
