package com.eynnzerr.cpbookkeeping_compose.model

data class HomeData(
    val monthlyExpense: Float = 0f,
    val monthlyRevenue: Float = 0f,
    val dailyExpense: Float = 0f,
    val dailyRevenue: Float = 0f,
    val budget: Float = 0f
)
