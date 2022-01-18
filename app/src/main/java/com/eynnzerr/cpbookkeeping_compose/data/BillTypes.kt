package com.eynnzerr.cpbookkeeping_compose.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.eynnzerr.cpbookkeeping_compose.R

/**
 * icons and labels
 */
val billTypes = mapOf<Int, String>(
    R.drawable.type_clothes to "服饰",
    R.drawable.type_communication to "通讯",
    R.drawable.type_daily to "日用",
    R.drawable.type_entertainment to "娱乐",
    R.drawable.type_food to "餐饮",
    R.drawable.type_house to "住房",
    R.drawable.type_medicine to "医疗",
    R.drawable.type_other to "其他",
    R.drawable.type_shopping to "购物",
    R.drawable.type_social to "人情",
    R.drawable.type_study to "学习",
    R.drawable.type_supply to "水电",
    R.drawable.type_tea to "烟酒",
    R.drawable.type_traffic to "交通",
    R.drawable.type_income to "收入",
    R.drawable.type_bonus to "奖金",
    R.drawable.type_borrow to "借入",
    R.drawable.type_debt to "收债",
    R.drawable.type_interest to "利息",
    R.drawable.type_investment to "投资",
    R.drawable.type_accident to "意外"
)

@RequiresApi(Build.VERSION_CODES.O)
val expenseList = listOf<Bill>(
    Bill(
        type = R.drawable.type_other,
        category = -1
    ),
    Bill(
        type = R.drawable.type_clothes,
        category = -1
    ),
    Bill(
        type = R.drawable.type_communication,
        category = -1
    ),
    Bill(
        type = R.drawable.type_daily,
        category = -1
    ),
    Bill(
        type = R.drawable.type_entertainment,
        category = -1
    ),
    Bill(
        type = R.drawable.type_food,
        category = -1
    ),
    Bill(
        type = R.drawable.type_house,
        category = -1
    ),
    Bill(
        type = R.drawable.type_medicine,
        category = -1
    ),
    Bill(
        type = R.drawable.type_social,
        category = -1
    )
)

val revenueList = listOf<Bill>(
    Bill(
        type = R.drawable.type_other,
        category = 1
    ),
    Bill(
        type = R.drawable.type_income,
        category = 1
    ),
    Bill(
        type = R.drawable.type_borrow,
        category = 1
    ),
    Bill(
        type = R.drawable.type_debt,
        category = 1
    ),
    Bill(
        type = R.drawable.type_interest,
        category = 1
    ),
    Bill(
        type = R.drawable.type_investment,
        category = 1
    ),
    Bill(
        type = R.drawable.type_accident,
        category = 1
    )
)

