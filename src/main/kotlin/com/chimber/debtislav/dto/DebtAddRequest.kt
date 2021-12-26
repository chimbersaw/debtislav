package com.chimber.debtislav.dto

data class DebtAddRequest(
    val groupId: Long,
    val lender: String,
    val amount: Int,
    val description: String?
)
