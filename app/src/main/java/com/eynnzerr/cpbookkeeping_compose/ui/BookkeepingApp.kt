package com.eynnzerr.cpbookkeeping_compose.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.eynnzerr.cpbookkeeping_compose.ui.basic.BasicScreen
import com.eynnzerr.cpbookkeeping_compose.ui.navigation.NavActions
import com.eynnzerr.cpbookkeeping_compose.ui.theme.CPBookkeepingcomposeTheme

@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun BookkeepingApp() {
    CPBookkeepingcomposeTheme {
        val navController = rememberNavController()
        BasicScreen(
            navController = navController
        )
    }
}