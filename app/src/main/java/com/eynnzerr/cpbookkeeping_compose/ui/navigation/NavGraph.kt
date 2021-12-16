package com.eynnzerr.cpbookkeeping_compose.ui.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeScreen
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.record.RecordScreen
import com.eynnzerr.cpbookkeeping_compose.ui.setting.SettingScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    listState: LazyListState = rememberLazyListState(),
    startDestination: String = Destinations.HOME_ROUTE
) {
    //Obtain each viewModel via Hilt in NavHost and only expose uiState of viewModel to Ui(i.e. composables)
    NavHost(navController = navController, startDestination = "home") {
        composable(Destinations.HOME_ROUTE){
            val homeViewModel: HomeViewModel = hiltViewModel()
            val uiState by homeViewModel.uiState.collectAsState()
            HomeScreen(uiState, listState)
        }
        composable(Destinations.RECORD_ROUTE){ RecordScreen() }
        composable(Destinations.SETTING_ROUTE){ SettingScreen() }
    }
}