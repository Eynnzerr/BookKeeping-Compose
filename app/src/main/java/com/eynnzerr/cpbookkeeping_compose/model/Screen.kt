package com.eynnzerr.cpbookkeeping_compose.model

import com.eynnzerr.cpbookkeeping_compose.ui.navigation.Destinations

//sealed class for bottom navigation items
sealed class Screen(val route: String, val label: String) {
    object Home: Screen(Destinations.HOME_ROUTE,"主页")
    object Record: Screen(Destinations.RECORD_ROUTE,"浏览")
    object Setting: Screen(Destinations.SETTING_ROUTE,"设置")
    object NewBill: Screen(Destinations.NEW_ROUTE,"新建")
}
