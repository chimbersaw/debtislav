package com.chimber.debtislav.dto

data class DebtRequest(
    val groupId: Long,
    val loaner: String,
    val amount: Int,
    val description: String?
)
