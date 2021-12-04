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
    R.drawable.type_traffic to "交通"
)

@RequiresApi(Build.VERSION_CODES.O)
val fakeList = listOf<Bill>(
    Bill(
        type = R.drawable.type_other,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_clothes,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_communication,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_daily,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_entertainment,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_food,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_house,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_medicine,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    ),
    Bill(
        type = R.drawable.type_social,
        amount = 10.00f,
        remark = "fuck u",
        category = -1
    )
)