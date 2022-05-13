package com.kis.bettingcurrency.ui.feature.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kis.bettingcurrency.data.repository.CurrencyRepository
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import com.kis.bettingcurrency.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val dataSource: CurrencyRepository,
) : ViewModel() {

    private var baseCurrency: Currency = Currency("EUR")

    private val _stateUI: MutableStateFlow<CurrenciesContract> = MutableStateFlow(
        CurrenciesContract(
            symbolsUIState = UIState.Loading,
            ratesUIState = UIState.Loading,
        )
    )
    val stateUI: StateFlow<CurrenciesContract>
        get() {
            return _stateUI
        }

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.value = stateUI.value.copy(
                symbolsUIState = UIState.Loading,
                ratesUIState = UIState.Loading,
            )
            val currenciesResult = dataSource.getCurrencies()

            baseCurrency = currenciesResult.find { currency -> currency == baseCurrency }
                ?: baseCurrency

            _stateUI.value = stateUI.value.copy(
                symbolsUIState = UIState.Success(
                    Symbols(
                        currencies = currenciesResult,
                        selectedCurrency = currenciesResult.first(),
                    )
                ),
            )
            loadRates()
        }
    }

    private fun loadRates() {
        viewModelScope.launch {
            _stateUI.value = _stateUI.value.copy(
                ratesUIState = UIState.Loading,
            )
            val result = dataSource.getRates(baseCurrency)

            _stateUI.value = stateUI.value.copy(
                ratesUIState = UIState.Success(
                    Rates(
                        result
                    )
                )
            )
        }
    }

    fun onSelectedCurrency(selectedCurrency: Currency) {
        baseCurrency = selectedCurrency

        (stateUI.value.symbolsUIState as? UIState.Success)?.let { symbolsUIState ->
            _stateUI.value = _stateUI.value.copy(
                symbolsUIState = symbolsUIState.copy(
                    data = symbolsUIState.data.copy(
                        selectedCurrency = selectedCurrency,
                    )
                )
            )
        }

        loadRates()
    }

    fun onClickFavouriteIcon(currencyRate: CurrencyRate) {
        viewModelScope.launch {
            if (currencyRate.isFavourite) {
                dataSource.removeFavourite(currencyRate.currency)
            } else {
                dataSource.addFavourite(currencyRate.currency)
            }

            (stateUI.value.ratesUIState as? UIState.Success)?.let { ratesUIState ->

                val newList = with(ratesUIState.data.currencies.toMutableList()) {
                    val index = this.indexOf(currencyRate)
                    this[index] = currencyRate.copy(
                        isFavourite = !currencyRate.isFavourite
                    )
                    return@with this
                }

                _stateUI.value = _stateUI.value.copy(
                    ratesUIState = ratesUIState.copy(
                        data = ratesUIState.data.copy(
                            currencies = newList
                        )
                    )
                )
            }
        }
    }
}