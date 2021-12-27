package com.chimber.debtislav.controller;


import com.chimber.debtislav.dto.DebtAddRequest
import com.chimber.debtislav.dto.DebtIdRequest
import com.chimber.debtislav.dto.DebtSuggestionRequest
import com.chimber.debtislav.repository.UserRepository
import com.chimber.debtislav.service.DebtService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/debt")
class DebtController(
    userRepository: UserRepository,
    private val debtService: DebtService
) : AbstractUserController(userRepository) {
    @PostMapping("/request")
    fun addDebtRequest(@RequestBody request: DebtSuggestionRequest) {
        debtService.requestDebt(currentUser, request)
    }

    @PostMapping("/add")
    fun addOwnDebt(@RequestBody request: DebtAddRequest) {
        debtService.addOwnDebt(currentUser, request)
    }

    @PostMapping("/respond")
    fun acceptDebt(@RequestBody request: DebtIdRequest) {
        debtService.acceptDebt(currentUser, request.id)
    }

    @DeleteMapping("/respond")
    fun rejectDebt(@RequestBody request: DebtIdRequest) {
        debtService.rejectDebt(currentUser, request.id)
    }

    @PostMapping("/close")
    fun closeDebt(@RequestBody request: DebtIdRequest) {
        debtService.closeDebt(currentUser, request.id)
    }
}
