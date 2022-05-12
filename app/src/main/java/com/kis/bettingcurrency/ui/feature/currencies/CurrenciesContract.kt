package com.kis.bettingcurrency.ui.feature.currencies

import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate

class CurrenciesContract {
    data class State(
        val currencies: List<CurrencyRate>,
        val symbols: List<Currency>
    )
}
