package com.chimber.debtislav.security.service

import com.chimber.debtislav.model.User
import com.chimber.debtislav.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    fun getUserOrThrow(username: String): User {
        return userRepository.findByUsername(username).orElseThrow {
            UsernameNotFoundException("No user with name $username exists")
        }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return findByName(username)
    }

    fun findByName(username: String): User {
        return getUserOrThrow(username)
    }

    fun updateToken(username: String, token: String?) {
        val user: User = findByName(username)
        user.setToken(token)
        userRepository.save(user)
    }
}
