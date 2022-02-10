package com.eynnzerr.cpbookkeeping_compose.model

import com.eynnzerr.cpbookkeeping_compose.data.BillStatistic

data class DetailData(
    val lineDataMonthly: List<BillStatistic> = emptyList(),  // 当前月份中各天的支出总额
    val lineDataYearly: List<BillStatistic> = emptyList(),  // 当前年份中各月的支出总额
    val pieDataMonthly: List<BillStatistic> = emptyList(),  // 当前月份中各种支出各自的总额
    val pieDataYearly: List<BillStatistic> = emptyList()  //当前年份中各种支出各自的总额
) {
    override fun toString(): String {
        return "DetailData(lineDataMonthly=$lineDataMonthly,\n lineDataYearly=$lineDataYearly,\n pieDataMonthly=$pieDataMonthly,\n pieDataYearly=$pieDataYearly\n)"
    }
}
