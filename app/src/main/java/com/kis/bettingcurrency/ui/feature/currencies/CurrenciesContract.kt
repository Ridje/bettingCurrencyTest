package com.kis.bettingcurrency.ui.feature.currencies

import com.kis.bettingcurrency.core.SortRateStrategy
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import com.kis.bettingcurrency.ui.UIState

data class CurrenciesContract(
    val symbolsUIState: UIState<Symbols>,
    val ratesUIState: UIState<Rates>,
    val onlyFavourite: Boolean,
    val sortRateStrategy: SortRateStrategy,
) {
    data class Symbols(
        val currencies: List<Currency>,
        val selectedCurrency: Currency,
    )

    data class Rates(
        val currencies: List<CurrencyRate>,
    )
}