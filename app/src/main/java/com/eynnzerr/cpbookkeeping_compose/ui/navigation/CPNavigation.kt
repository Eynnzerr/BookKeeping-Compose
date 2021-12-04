package com.eynnzerr.cpbookkeeping_compose.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

object Destinations {
    const val HOME_ROUTE = "home"
    const val RECORD_ROUTE = "record"
    const val SETTING_ROUTE = "setting"
}

class NavActions(navController: NavController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(Destinations.HOME_ROUTE) {
            navigateTo(navController, Destinations.HOME_ROUTE)
        }
    }

    val navigateToRecord: () -> Unit = {
        navController.navigate(Destinations.RECORD_ROUTE) {
            navigateTo(navController, Destinations.RECORD_ROUTE)
        }
    }

    val navigateToSetting: () -> Unit = {
        navController.navigate(Destinations.SETTING_ROUTE) {
            navigateTo(navController, Destinations.SETTING_ROUTE)
        }

    }

    /**
     * Navigate to the given destination with SingleTop mode and restoring state.
     */
    private fun navigateTo(navController: NavController, route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}