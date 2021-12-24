package com.chimber.debtislav.repository

import com.chimber.debtislav.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
@Transactional
interface UserRepository : JpaRepository<AppUser, Long> {
    fun findByUsername(username: String): Optional<AppUser>

    fun findByEmail(username: String): Optional<AppUser>
}
