package com.chimber.debtislav.controller;


import com.chimber.debtislav.dto.DebtRequest
import com.chimber.debtislav.service.DebtService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/debt")
class DebtController(private val debtService: DebtService) : Controller() {
    @PostMapping("/request")
    fun addUser(@RequestBody request: DebtRequest) {
        debtService.requestDebt(currentUsername, request)
    }
}
