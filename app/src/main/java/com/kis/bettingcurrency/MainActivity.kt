package com.kis.bettingcurrency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import com.kis.bettingcurrency.ui.feature.currencies.CurrenciesContract
import com.kis.bettingcurrency.ui.feature.currencies.CurrenciesScreen
import com.kis.bettingcurrency.ui.theme.BettingCurrencyTheme
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BettingCurrencyTheme {
                CurrenciesScreen(
                    state = CurrenciesContract.State(
                        listOfCurrencyRate(60),
                        listOfSymbols(3),
                    )
                )
            }
        }
    }
}

private fun listOfCurrencyRate(count: Int): List<CurrencyRate> {
    val list: MutableList<CurrencyRate> = mutableListOf()

    for (i in 1..count) {
        list.add(
            CurrencyRate(Currency("VAL$i", i % 2 == 0), BigDecimal(i))
        )
    }
    list.sortBy { it.rate }
    return list
}

private fun listOfSymbols(count: Int): List<Currency> {
    val list: MutableList<Currency> = mutableListOf()

    for (i in 1..count) {
        list.add(
            Currency("VAL$i", i % 2 == 0),
        )
    }
    return list
}
