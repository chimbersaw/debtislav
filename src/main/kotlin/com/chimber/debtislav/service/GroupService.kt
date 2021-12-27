package com.chimber.debtislav.service

import com.chimber.debtislav.exception.NotAuthorizedException
import com.chimber.debtislav.model.Debt
import com.chimber.debtislav.model.Group
import com.chimber.debtislav.model.User
import com.chimber.debtislav.repository.GroupRepository
import org.springframework.stereotype.Service


@Service
class GroupService(
    private val groupRepository: GroupRepository
) {
    fun addToGroup(currentUser: User, userToAdd: User, groupId: Long) {
        val group = groupRepository.getGroupOrThrow(groupId)
        if (currentUser.id != group.admin_id) throw NotAuthorizedException()

        group.userList.add(userToAdd)
        groupRepository.save(group)
    }

    fun createGroup(user: User, groupName: String): Long {
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

    fun getAllDebts(groupId: Long): List<Debt> {
        val group = groupRepository.getGroupOrThrow(groupId)
        return group.debtList.toList()
    }
}
