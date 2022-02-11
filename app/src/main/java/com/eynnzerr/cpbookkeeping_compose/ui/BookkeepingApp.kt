package com.eynnzerr.cpbookkeeping_compose.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.eynnzerr.cpbookkeeping_compose.ui.basic.BasicScreen
import com.eynnzerr.cpbookkeeping_compose.ui.navigation.NavActions
import com.eynnzerr.cpbookkeeping_compose.ui.theme.Blue_Sky
import com.eynnzerr.cpbookkeeping_compose.ui.theme.CPBookkeepingcomposeTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun BookkeepingApp() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Blue_Sky.copy(alpha = 0.85f),
            darkIcons = useDarkIcons
        )
    }

    CPBookkeepingcomposeTheme {
        val navController = rememberNavController()
        BasicScreen(
            navController = navController
        )
    }
}