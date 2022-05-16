package com.kis.bettingcurrency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kis.bettingcurrency.ui.CurrencyMainScreen
import com.kis.bettingcurrency.ui.theme.BettingCurrencyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BettingCurrencyTheme {
                CurrencyMainScreen()
            }
        }
    }
}
