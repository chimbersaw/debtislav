package com.chimber.debtislav.service

import com.chimber.debtislav.dto.RegistrationRequest
import com.chimber.debtislav.model.Debt
import com.chimber.debtislav.model.User
import com.chimber.debtislav.repository.GroupRepository
import com.chimber.debtislav.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(registrationRequest: RegistrationRequest) {
        val emailExists = userRepository.findByEmail(registrationRequest.email).isPresent
        if (emailExists) {
            throw IllegalStateException("User with this email already exists")
        }
        val usernameExists = userRepository.findByUsername(registrationRequest.username).isPresent
        if (usernameExists) {
            throw IllegalStateException("User with this name already exists")
        }
        val encodedPassword: String = passwordEncoder.encode(registrationRequest.password)
        val user = User(
            email = registrationRequest.email,
            username = registrationRequest.username,
            password = encodedPassword,
        )
        userRepository.save(user)
    }

    fun getAllGroups(user: User): List<String> {
        return user.groupList.map { it.name }.toList()
    }

    fun getMyDebts(user: User, groupId: Long): List<Debt> {
        val group = groupRepository.getGroupOrThrow(groupId)
        return group.debtList.filter { it.loaner_id == user.id }
    }

    fun getDebtsToMe(user: User, groupId: Long): List<Debt> {
        val group = groupRepository.getGroupOrThrow(groupId)
        return group.debtList.filter { it.lender_id == user.id }
    }
}
