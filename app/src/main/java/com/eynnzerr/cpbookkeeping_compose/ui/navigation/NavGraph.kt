package com.eynnzerr.cpbookkeeping_compose.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeScreen
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.addbill.NewScreen
import com.eynnzerr.cpbookkeeping_compose.ui.addbill.NewViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.addbill.rememberTabContent
import com.eynnzerr.cpbookkeeping_compose.ui.detail.DetailScreen
import com.eynnzerr.cpbookkeeping_compose.ui.detail.DetailViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.display.DisplayScreen
import com.eynnzerr.cpbookkeeping_compose.ui.display.DisplayViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.record.RecordScreen
import com.eynnzerr.cpbookkeeping_compose.ui.record.RecordViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.setting.SettingScreen
import com.eynnzerr.cpbookkeeping_compose.ui.setting.SettingViewModel

@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    listState: LazyListState = rememberLazyListState(),
    startDestination: String = Destinations.HOME_ROUTE
) {
    NavHost(navController = navController, startDestination = "home", route = "root") {
        composable(Destinations.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val uiState by homeViewModel.uiState.collectAsState()
            val month = homeViewModel.currentMonth
            val year = homeViewModel.currentYear
            HomeScreen(
                uiState = uiState,
                listState =  listState,
                openAnalysis = { navController.navigateTo(Destinations.DETAIL_ROUTE + "/$month/$year") },
                loadData = { homeViewModel.loadData() },
                onDeleteBill = { bill ->
                    homeViewModel.deleteBill(bill)
                    homeViewModel.loadData()
                },
                openDisplay = { bill ->
                    navController.navigateToSingle(Destinations.DISPLAY_ROUTE + "/${bill.id}")
                }
            )
        }
        composable(Destinations.RECORD_ROUTE) {
            val recordViewModel: RecordViewModel = hiltViewModel()
            val uiState by recordViewModel.uiState.collectAsState()
            RecordScreen(
                uiState = uiState,
                listState = listState,
                updateBills = { recordViewModel.updateBills() },
                onDeleteBill = { bill ->
                    recordViewModel.deleteBill(bill)
                    recordViewModel.updateBills()
                },
                openAnalysis = { month, year ->
                    navController.navigateTo(Destinations.DETAIL_ROUTE + "/$month/$year") },
                openDisplay = { bill ->
                    navController.navigateToSingle(Destinations.DISPLAY_ROUTE + "/${bill.id}")
                }
            )
        }
        composable(Destinations.SETTING_ROUTE) {
            val settingViewModel: SettingViewModel = hiltViewModel()
            val uiState by settingViewModel.uiState.collectAsState()
            SettingScreen(uiState = uiState, add = {settingViewModel.add()})
        }
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
        composable(
            route = Destinations.DETAIL_ROUTE + "/{month}/{year}",
            arguments = listOf(
                navArgument("month") { type = NavType.IntType },
                navArgument("year") { type = NavType.IntType }
            )
        ) {
            // How to pass constructor arguments to HiltViewModel?
            val category = remember { mutableStateOf(-1) }
            val detailViewModel = hiltViewModel<DetailViewModel>().apply {
                month = it.arguments?.getInt("month")!!
                year = it.arguments?.getInt("year")!!
                loadData(category.value)
            }
            val uiState by detailViewModel.uiState.collectAsState()
            DetailScreen(
                uiState = uiState,
                loadData = { mCategory ->
                    detailViewModel.loadData(mCategory)
                },
                category = category,
                openDisplay = { bill ->
                    navController.navigateToSingle(Destinations.DISPLAY_ROUTE + "/${bill.id}")
                }
            )
        }
        composable(
            route = Destinations.DISPLAY_ROUTE + "/{billId}",
            arguments = listOf(
                navArgument("billId") { type = NavType.IntType }
            )
        ) {
            val displayViewModel: DisplayViewModel = hiltViewModel()
            val id = it.arguments?.getInt("billId")!!
            displayViewModel.loadBillById(id)
            val bill by displayViewModel.billState.collectAsState()
            DisplayScreen(bill = bill)
        }
    }
}