package com.chimber.debtislav.service

import com.chimber.debtislav.dto.DebtAddRequest
import com.chimber.debtislav.dto.DebtSuggestionRequest
import com.chimber.debtislav.dto.GroupDebtSuggestionRequest
import com.chimber.debtislav.exception.DebtNotFoundException
import com.chimber.debtislav.exception.GroupNotFoundException
import com.chimber.debtislav.exception.WrongAmountException
import com.chimber.debtislav.exception.WrongUserRelationException
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
    private fun checkUserInGroupOrThrow(user: User, groupId: Long, name: String = "User") {
        if (!user.groupList.map { it.id }.contains(groupId)) {
            throw GroupNotFoundException("$name does not belong to group with id $groupId")
        }
    }

    private fun checkAmountPositiveOrThrow(amount: Int) {
        if (amount <= 0) {
            throw WrongAmountException("Amount must be positive but is actually $amount")
        }
    }

    private fun getDebtGroupOrThrow(user: User, debtId: Long): Group {
        user.groupList.forEach { group ->
            if (group.debtList.any { it.id == debtId }) {
                return group
            }
        }
        throw DebtNotFoundException("Debt with id $debtId not found in user groups")
    }

    fun requestDebt(user: User, request: DebtSuggestionRequest) {
        val group = groupRepository.getGroupOrThrow(request.groupId)
        val loaner = userRepository.getUserOrThrow(request.loaner)

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

    fun requestGroupDebt(user: User, request: GroupDebtSuggestionRequest) {
        val group = groupRepository.getGroupOrThrow(request.groupId)

        checkAmountPositiveOrThrow(request.amount)
        checkUserInGroupOrThrow(user, group.id)

        val groupSize = group.userList.size
        if (groupSize == 1) {
            throw GroupNotFoundException("Group size must be at least two to request a group debt.")
        }

        val eachAmount = request.amount / groupSize
        // The remainder is paid by the user (0 <= remainder < groupSize)
        for (loaner in group.userList) {
            if (loaner == user) continue

            val debt = Debt(
                group_id = group.id,
                amount = eachAmount,
                lender_id = user.id,
                loaner_id = loaner.id,
                description = request.description ?: "",
                status = DebtStatus.REQUESTED
            )
            debtRepository.save(debt)
        }
    }

    fun addOwnDebt(user: User, request: DebtAddRequest) {
        val group = groupRepository.getGroupOrThrow(request.groupId)
        val lender = userRepository.getUserOrThrow(request.lender)

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

    fun acceptDebt(user: User, debtId: Long) {
        val debt = getDebtGroupOrThrow(user, debtId).debtList.first { it.id == debtId }
        if (debt.loaner_id != user.id) {
            throw WrongUserRelationException("You must be a debt's loaner to accept it.")
        }
        debt.status = DebtStatus.ACTIVE
        debtRepository.save(debt)
    }

    fun rejectDebt(user: User, debtId: Long) {
        val group = getDebtGroupOrThrow(user, debtId)
        val debt = group.debtList.first { it.id == debtId }
        if (user.id != debt.loaner_id && user.id != debt.lender_id) {
            throw WrongUserRelationException("You must be a debt's loaner or lender to reject it.")
        }
        group.debtList.removeIf { it.id == debt.id }
        groupRepository.save(group)
        debtRepository.delete(debt)
    }

    fun closeDebt(user: User, debtId: Long) {
        val debt = getDebtGroupOrThrow(user, debtId).debtList.first { it.id == debtId }
        if (debt.lender_id != user.id) {
            throw WrongUserRelationException("You must be a debt's lender to close it.")
        }
        debt.status = DebtStatus.PAID
        debtRepository.save(debt)
    }
}
