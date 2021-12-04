package com.eynnzerr.cpbookkeeping_compose.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.eynnzerr.cpbookkeeping_compose.ui.basic.BasicScreen
import com.eynnzerr.cpbookkeeping_compose.ui.navigation.NavActions
import com.eynnzerr.cpbookkeeping_compose.ui.theme.CPBookkeepingcomposeTheme

@ExperimentalAnimationApi
@Composable
fun BookkeepingApp() {
    CPBookkeepingcomposeTheme {
        val navController = rememberNavController()
        val navActions = remember(navController) {
            NavActions(navController)
        }
        BasicScreen(
            navController = navController
        )
    }
}