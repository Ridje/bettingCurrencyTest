package com.kis.bettingcurrency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.kis.bettingcurrency.ui.feature.currencies.CurrenciesScreenRoute
import com.kis.bettingcurrency.ui.feature.currencies.CurrenciesViewModel
import com.kis.bettingcurrency.ui.theme.BettingCurrencyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: CurrenciesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BettingCurrencyTheme {
                CurrenciesScreenRoute(
                    viewModel = viewModel,
                )
            }
        }
    }
}
