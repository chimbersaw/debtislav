package com.chimber.debtislav.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class DebtNotFoundException(reason: String) : ResponseStatusException(HttpStatus.NOT_FOUND, reason)
