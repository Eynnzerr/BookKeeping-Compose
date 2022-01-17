package com.eynnzerr.cpbookkeeping_compose.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeScreen
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.addbill.NewScreen
import com.eynnzerr.cpbookkeeping_compose.ui.addbill.NewViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.addbill.rememberTabContent
import com.eynnzerr.cpbookkeeping_compose.ui.record.RecordScreen
import com.eynnzerr.cpbookkeeping_compose.ui.record.RecordViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.setting.SettingScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    listState: LazyListState = rememberLazyListState(),
    startDestination: String = Destinations.HOME_ROUTE
) {
    //Obtain each viewModel via Hilt in NavHost and only expose uiState of viewModel to Ui(i.e. composables)
    NavHost(navController = navController, startDestination = "home", route = "root") {
        composable(Destinations.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val uiState by homeViewModel.uiState.collectAsState()
            HomeScreen(
                uiState = uiState,
                listState =  listState,
                updateBills = { homeViewModel.updateBills() },
                onDeleteBill = { bill ->
                    homeViewModel.deleteBill(bill)
                    homeViewModel.updateBills()
                }
            )
        }
        composable(Destinations.RECORD_ROUTE) {
            val recordViewModel: RecordViewModel = hiltViewModel()
            RecordScreen()
        }
        composable(Destinations.SETTING_ROUTE) { SettingScreen() }
        composable(Destinations.NEW_ROUTE) {
            val newViewModel: NewViewModel = hiltViewModel()
            val remark by newViewModel.remarkState.collectAsState()
            val expenseState by newViewModel.exTabState.collectAsState()
            val revenueState by newViewModel.reTabState.collectAsState()

            val scaffoldState = rememberBottomSheetScaffoldState()
            val scope = rememberCoroutineScope()
            val tabContent = rememberTabContent(
                scaffoldState = scaffoldState,
                scope = scope,
                expenseState = expenseState,
                revenueState = revenueState,
                onUpdate = {category, amount, selectedIndex -> newViewModel.updateTab(category, amount, selectedIndex)},
                remark = remark,
                onRemarkChange = { newRemark -> newViewModel.updateRemark(newRemark) },
                onSubmit = { bill ->
                    newViewModel.insertBill(bill)
                    navController.popBackStack()
                }
            )
            val (currentSection, updateSection) = rememberSaveable {
                mutableStateOf(tabContent.first().section)
            }
            NewScreen(
                scaffoldState = scaffoldState,
                scope = scope,
                remark = remark,
                tabContent = tabContent,
                currentSection = currentSection,
                onTabChange = updateSection,
                onRemarkChange = { newRemark -> newViewModel.updateRemark(newRemark) }
            )
        }
    }
}