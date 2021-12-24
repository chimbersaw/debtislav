package com.chimber.debtislav.util

import org.springframework.stereotype.Service


@Service
class EmailValidator {
    fun validate(email: String): Boolean {
        return Regex(EMAIL_PATTERN).matches(email)
    }

    companion object {
        private const val EMAIL_PATTERN = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$"
    }
}
