package com.chimber.debtislav.service

import com.chimber.debtislav.dto.DebtRequest
import com.chimber.debtislav.exception.GroupNotFoundException
import com.chimber.debtislav.exception.UserNotFoundException
import com.chimber.debtislav.exception.WrongAmountException
import com.chimber.debtislav.model.Debt
import com.chimber.debtislav.model.DebtStatus
import com.chimber.debtislav.model.Group
import com.chimber.debtislav.model.User
import com.chimber.debtislav.repository.DebtRepository
import com.chimber.debtislav.repository.GroupRepository
import com.chimber.debtislav.repository.UserRepository
import org.springframework.stereotype.Service


@Service
class DebtService(
    private val debtRepository: DebtRepository,
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    fun getUserOrThrow(username: String): User {
        return userRepository.findByUsername(username).orElseThrow {
            UserNotFoundException("No user with name $username exists")
        }
    }

    fun getGroupOrThrow(groupId: Long): Group {
        return groupRepository.findById(groupId).orElseThrow {
            GroupNotFoundException("No group with id $groupId exists")
        }
    }

    fun userInGroup(user: User, groupId: Long): Boolean {
        return user.groupList.map { it.id }.contains(groupId)
    }

    fun requestDebt(currentName: String, request: DebtRequest) {
        val user = getUserOrThrow(currentName)
        val group = getGroupOrThrow(request.groupId)
        if (!userInGroup(user, group.id)) {
            throw GroupNotFoundException("User does not belong to group with id ${group.id}")
        }

        val loaner = getUserOrThrow(request.loaner)
        if (!userInGroup(loaner, group.id)) {
            throw GroupNotFoundException("Loaner does not belong to group with id ${group.id}")
        }

        val amount = request.amount
        if (amount <= 0) {
            throw WrongAmountException("Amount must be positive but is actually $amount")
        }

        val debt = Debt(
            group_id = group.id,
            amount = amount,
            lender_id = user.id,
            loaner_id = loaner.id,
            description = request.description ?: "",
            status = DebtStatus.REQUESTED
        )
        debtRepository.save(debt)
    }
}
