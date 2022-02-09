package com.eynnzerr.cpbookkeeping_compose.model

import com.eynnzerr.cpbookkeeping_compose.data.BillStatistic

data class DetailData(
    val lineDataMonthly: List<BillStatistic> = emptyList(),
    val lineDataYearly: List<BillStatistic> = emptyList(),
    val pieDataMonthly: List<BillStatistic> = emptyList(),
    val pieDataYearly: List<BillStatistic> = emptyList()
)
