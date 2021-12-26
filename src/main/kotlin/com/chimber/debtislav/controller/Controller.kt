package com.chimber.debtislav.controller

import com.chimber.debtislav.exception.NotAuthorizedException
import org.springframework.security.core.context.SecurityContextHolder

abstract class Controller {
    protected val currentUsername
        get() = SecurityContextHolder.getContext().authentication?.principal?.toString()
            ?: throw NotAuthorizedException()
}
