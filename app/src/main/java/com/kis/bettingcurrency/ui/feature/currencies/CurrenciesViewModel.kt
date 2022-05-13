package com.kis.bettingcurrency.ui.feature.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kis.bettingcurrency.data.repository.CurrencyRepository
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

    private var baseCurrency = "EUR"

    val _stateUI: MutableStateFlow<CurrenciesContract> = MutableStateFlow(
        CurrenciesContract(
            symbolsUIState = UIState.Loading,
            ratesUIState = UIState.Loading,
        )
    )
    val stateUI: StateFlow<CurrenciesContract> = _stateUI

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
            baseCurrency = currenciesResult.first().ISO
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
        viewModelScope.launch(Dispatchers.IO) {
            _stateUI.value = stateUI.value.copy(
                ratesUIState = UIState.Success(
                    Rates(
                        dataSource.getRates(baseCurrency)
                    )
                )
            )
        }
    }
}