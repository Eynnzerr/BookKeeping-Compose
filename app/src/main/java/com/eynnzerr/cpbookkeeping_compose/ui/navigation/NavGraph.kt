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
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeScreen
import com.eynnzerr.cpbookkeeping_compose.ui.home.HomeViewModel
import com.eynnzerr.cpbookkeeping_compose.ui.new.NewScreen
import com.eynnzerr.cpbookkeeping_compose.ui.new.rememberTabContent
import com.eynnzerr.cpbookkeeping_compose.ui.record.RecordScreen
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
    NavHost(navController = navController, startDestination = "home") {
        composable(Destinations.HOME_ROUTE){
            val homeViewModel: HomeViewModel = hiltViewModel()
            val uiState by homeViewModel.uiState.collectAsState()
            HomeScreen(uiState, listState)
        }
        composable(Destinations.RECORD_ROUTE){ RecordScreen() }
        composable(Destinations.SETTING_ROUTE){ SettingScreen() }
        composable(Destinations.NEW_ROUTE){
            //TODO 可以将新建收入与支出的scaffoldState和remark在viewModel里区分开，分别传入。
            //为了能在切换Tab时保留原页面的项目，可以也将其作为state存在viewModel里。

            val scaffoldState = rememberBottomSheetScaffoldState()
            val scope = rememberCoroutineScope()
            val remark = remember{ mutableStateOf("添加备注") }
            val tabContent = rememberTabContent(scaffoldState = scaffoldState, scope = scope, remark = remark)
            val (currentSection, updateSection) = rememberSaveable {
                mutableStateOf(tabContent.first().section)
            }
            NewScreen(
                scaffoldState = scaffoldState,
                scope = scope,
                remark = remark,
                tabContent = tabContent,
                currentSection = currentSection,
                onTabChange = updateSection
            )
        }
    }
}