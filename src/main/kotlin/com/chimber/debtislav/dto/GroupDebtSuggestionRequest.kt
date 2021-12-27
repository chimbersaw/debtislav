package com.chimber.debtislav.dto

data class GroupDebtSuggestionRequest(
    val groupId: Long,
    val amount: Int,
    val description: String?
)
