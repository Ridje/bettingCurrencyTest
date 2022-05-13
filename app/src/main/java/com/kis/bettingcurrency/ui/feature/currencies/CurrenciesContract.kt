package com.kis.bettingcurrency.ui.feature.currencies

import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate

data class CurrenciesContract(
    val symbolsUIState: UIState<Symbols>,
    val ratesUIState: UIState<Rates>,
)

data class Symbols(
    val currencies: List<Currency>,
    val selectedCurrency: Currency,
)

data class Rates(
    val currencies: List<CurrencyRate>,
)


sealed class UIState<out T : Any> {
    data class Success<out T: Any>(val data: T) : UIState<T>()
    object Loading : UIState<Nothing>()
    class Error(val message: String) : UIState<Nothing>()
}
