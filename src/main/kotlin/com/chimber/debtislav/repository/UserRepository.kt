package com.chimber.debtislav.repository

import com.chimber.debtislav.exception.UserNotFoundException
import com.chimber.debtislav.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
@Transactional
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>

    fun findByEmail(username: String): Optional<User>

    fun getUserOrThrow(username: String): User {
        return findByUsername(username).orElseThrow {
            UserNotFoundException("No user with name $username exists")
        }
    }
}
