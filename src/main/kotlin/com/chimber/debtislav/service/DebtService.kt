package com.chimber.debtislav.service

import com.chimber.debtislav.dto.DebtAddRequest
import com.chimber.debtislav.dto.DebtSuggestionRequest
import com.chimber.debtislav.exception.*
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

    fun checkUserInGroupOrThrow(user: User, groupId: Long, name: String = "User") {
        if (!user.groupList.map { it.id }.contains(groupId)) {
            throw GroupNotFoundException("$name does not belong to group with id $groupId")
        }
    }

    fun checkAmountPositiveOrThrow(amount: Int) {
        if (amount <= 0) {
            throw WrongAmountException("Amount must be positive but is actually $amount")
        }
    }

    fun getDebtGroupOrThrow(username: String, debtId: Long): Group {
        val user = getUserOrThrow(username)
        user.groupList.forEach { group ->
            if (group.debtList.any { it.id == debtId }) {
                return group
            }
        }
        throw DebtNotFoundException("Debt with id $debtId not found in user groups")
    }

    fun requestDebt(currentName: String, request: DebtSuggestionRequest) {
        val user = getUserOrThrow(currentName)
        val group = getGroupOrThrow(request.groupId)
        val loaner = getUserOrThrow(request.loaner)

        checkUserInGroupOrThrow(user, group.id)
        checkUserInGroupOrThrow(loaner, group.id, "Loaner")
        checkAmountPositiveOrThrow(request.amount)

        val debt = Debt(
            group_id = group.id,
            amount = request.amount,
            lender_id = user.id,
            loaner_id = loaner.id,
            description = request.description ?: "",
            status = DebtStatus.REQUESTED
        )
        debtRepository.save(debt)
    }

    fun addOwnDebt(currentName: String, request: DebtAddRequest) {
        val user = getUserOrThrow(currentName)
        val group = getGroupOrThrow(request.groupId)
        val lender = getUserOrThrow(request.lender)

        checkUserInGroupOrThrow(user, group.id)
        checkUserInGroupOrThrow(lender, group.id, "Lender")
        checkAmountPositiveOrThrow(request.amount)

        val debt = Debt(
            group_id = group.id,
            amount = request.amount,
            lender_id = lender.id,
            loaner_id = user.id,
            description = request.description ?: "",
            status = DebtStatus.ACTIVE
        )
        debtRepository.save(debt)
    }

    fun acceptDebt(username: String, debtId: Long) {
        val user = getUserOrThrow(username)
        val debt = getDebtGroupOrThrow(username, debtId).debtList.first { it.id == debtId }
        if (debt.loaner_id != user.id) {
            throw WrongUserRelationException("You must be a debt's loaner to accept it.")
        }
        debt.status = DebtStatus.ACTIVE
        debtRepository.save(debt)
    }

    fun rejectDebt(username: String, debtId: Long) {
        val user = getUserOrThrow(username)
        val group = getDebtGroupOrThrow(username, debtId)
        val debt = group.debtList.first { it.id == debtId }
        if (debt.loaner_id != user.id) {
            throw WrongUserRelationException("You must be a debt's loaner to reject it.")
        }
        group.debtList.removeIf { it.id == debt.id }
        groupRepository.save(group)
        debtRepository.delete(debt)
    }

    fun closeDebt(username: String, debtId: Long) {
        val user = getUserOrThrow(username)
        val debt = getDebtGroupOrThrow(username, debtId).debtList.first { it.id == debtId }
        if (debt.lender_id != user.id) {
            throw WrongUserRelationException("You must be a debt's lender to close it.")
        }
        debt.status = DebtStatus.PAID
        debtRepository.save(debt)
    }
}
