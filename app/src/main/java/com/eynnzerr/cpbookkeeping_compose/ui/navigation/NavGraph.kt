package com.eynnzerr.cpbookkeeping_compose.ui.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeScreen
import com.eynnzerr.cpbookkeeping_compose.ui.record.RecordScreen
import com.eynnzerr.cpbookkeeping_compose.ui.setting.SettingScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    listState: LazyListState = rememberLazyListState(),
    startDestination: String = Destinations.HOME_ROUTE
) {
    NavHost(navController = navController, startDestination = "home") {
        composable(Destinations.HOME_ROUTE){ HomeScreen(listState) }
        composable(Destinations.RECORD_ROUTE){ RecordScreen() }
        composable(Destinations.SETTING_ROUTE){ SettingScreen() }
    }
}