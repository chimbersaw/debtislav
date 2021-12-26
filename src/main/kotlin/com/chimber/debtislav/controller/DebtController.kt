package com.chimber.debtislav.controller;


import com.chimber.debtislav.dto.DebtAddRequest
import com.chimber.debtislav.dto.DebtIdRequest
import com.chimber.debtislav.dto.DebtSuggestionRequest
import com.chimber.debtislav.service.DebtService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/debt")
class DebtController(private val debtService: DebtService) : Controller() {
    @PostMapping("/request")
    fun addDebtRequest(@RequestBody request: DebtSuggestionRequest) {
        debtService.requestDebt(currentUsername, request)
    }

    @PostMapping("/add")
    fun addOwnDebt(@RequestBody request: DebtAddRequest) {
        debtService.addOwnDebt(currentUsername, request)
    }

    @PostMapping("/respond")
    fun acceptDebt(@RequestBody request: DebtIdRequest) {
        debtService.acceptDebt(currentUsername, request.id)
    }

    @DeleteMapping("/respond")
    fun rejectDebt(@RequestBody request: DebtIdRequest) {
        debtService.rejectDebt(currentUsername, request.id)
    }

    @PostMapping("/close")
    fun closeDebt(@RequestBody request: DebtIdRequest) {
        debtService.closeDebt(currentUsername, request.id)
    }
}
