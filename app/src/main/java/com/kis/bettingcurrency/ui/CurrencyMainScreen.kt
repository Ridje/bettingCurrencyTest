package com.kis.bettingcurrency.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kis.bettingcurrency.ui.NavigationKeys.Route.CURRENCIES_LIST
import com.kis.bettingcurrency.ui.feature.currencies.CurrenciesScreenRoute


@Composable
fun CurrencyMainScreen() {
    val navController = rememberNavController()
    Scaffold(
    ) {
        CurrencyNavHost(navController)
    }
}


@Composable
fun CurrencyNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = CURRENCIES_LIST) {
        composable(route = CURRENCIES_LIST) {
            CurrenciesScreenRoute(navController)
        }
    }
}


object NavigationKeys {
    object Route {
        const val CURRENCIES_LIST = "currencies"
    }
}