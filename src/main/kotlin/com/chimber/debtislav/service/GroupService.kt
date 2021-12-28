package com.chimber.debtislav.service

import com.chimber.debtislav.exception.NotAuthorizedException
import com.chimber.debtislav.model.Debt
import com.chimber.debtislav.model.DebtStatus
import com.chimber.debtislav.model.Group
import com.chimber.debtislav.model.User
import com.chimber.debtislav.repository.DebtRepository
import com.chimber.debtislav.repository.GroupRepository
import com.chimber.debtislav.util.Edge
import com.chimber.debtislav.util.Graph
import org.springframework.stereotype.Service


@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val debtRepository: DebtRepository
) {
    fun addToGroup(currentUser: User, userToAdd: User, group: Group) {
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

    fun getAllUsers(group: Group): List<String> {
        return group.userList.map { it.toString() }.toList()
    }

    fun getAllDebts(group: Group): List<Debt> {
        return group.debtList.toList()
    }

    fun minimizeDebts(group: Group) {
        // maximum one debt for each person
        val debts = group.debtList.filter { it.status == DebtStatus.ACTIVE }
        val balance = mutableMapOf<Long, Int>()
        for (debt in debts) {
            balance.compute(debt.loaner_id) { _, v -> (v ?: 0) - debt.amount }
            balance.compute(debt.lender_id) { _, v -> (v ?: 0) + debt.amount }
        }
        val users = balance.keys.toList().sortedBy { balance[it] }
        var i = 0
        var j = users.size - 1
        val newDebts = mutableListOf<Debt>()
        while (i < j) {
            val loaner = users[i]
            val lender = users[j]

            if (balance[loaner] == 0) {
                i++
                continue
            } else if (balance[lender] == 0) {
                j--
                continue
            }

            val loanerAmount = balance[loaner] ?: throw AssertionError()
            val lenderAmount = balance[lender] ?: throw AssertionError()

            assert(loanerAmount < 0)
            assert(lenderAmount > 0)

            val amount = minOf(-loanerAmount, lenderAmount)
            if (amount == -loanerAmount) {
                i++
            }
            if (amount == lenderAmount) {
                j--
            }

            val debt = Debt(
                group_id = group.id,
                amount = amount,
                lender_id = lender,
                loaner_id = loaner,
                description = "Artificial debt after minimization",
                status = DebtStatus.ACTIVE
            )
            newDebts.add(debt)
            debtRepository.save(debt)
        }

        for (debt in debts) {
            group.debtList.removeIf { it.id == debt.id }
            debtRepository.delete(debt)
        }
        group.debtList.addAll(newDebts)
        groupRepository.save(group)
    }

    fun reduceCycles(group: Group) {
        // gets rid of all cycles
        val debts = group.debtList.filter { it.status == DebtStatus.ACTIVE }
        val edges = debts.map { Edge(it.loaner_id, it.lender_id, it.amount) }
        val graph = Graph(edges)
        graph.reduceAllCycles()
        val newDebts = graph.edges.map { edge ->
            val debt = Debt(
                group_id = group.id,
                amount = edge.weight,
                lender_id = edge.b,
                loaner_id = edge.a,
                description = "Artificial debt after reducing cycles",
                status = DebtStatus.ACTIVE
            )
            debtRepository.save(debt)
        }

        for (debt in debts) {
            group.debtList.removeIf { it.id == debt.id }
            debtRepository.delete(debt)
        }
        group.debtList.addAll(newDebts)
        groupRepository.save(group)
    }
}
