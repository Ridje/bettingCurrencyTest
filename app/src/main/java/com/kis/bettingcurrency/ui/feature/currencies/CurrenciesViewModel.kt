package com.kis.bettingcurrency.ui.feature.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kis.bettingcurrency.R
import com.kis.bettingcurrency.core.ResourceProvider
import com.kis.bettingcurrency.data.repository.CurrencyRepository
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import com.kis.bettingcurrency.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val dataSource: CurrencyRepository,
    private val resourceProvider: ResourceProvider,
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

    private val ratesExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _stateUI.value = _stateUI.value.copy(
            ratesUIState = UIState.Error(
                when (throwable) {
                    is HttpException -> {
                        resourceProvider.getString(R.string.rates_list_not_available, throwable.code())
                    }
                    else -> {
                        resourceProvider.getString(R.string.unknown_error)
                    }
                }
            ),
        )
    }

    private val currenciesExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _stateUI.value = _stateUI.value.copy(
            symbolsUIState = UIState.Error(
                when (throwable) {
                    is HttpException -> {
                        resourceProvider.getString(R.string.currencies_list_not_available, throwable.code())
                    }
                    else -> {
                        resourceProvider.getString(R.string.unknown_error)
                    }
                }
            ),
            ratesUIState = UIState.Error(),
        )
    }

    private var ratesJob: Job? = null

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch(currenciesExceptionHandler) {
            _stateUI.value = stateUI.value.copy(
                symbolsUIState = UIState.Loading,
                ratesUIState = UIState.Loading,
            )
            val currenciesResult = dataSource.getCurrencies()

            baseCurrency = currenciesResult.find { currency -> currency == baseCurrency }
                ?: baseCurrency

            _stateUI.value = stateUI.value.copy(
                symbolsUIState = UIState.Success(
                    CurrenciesContract.Symbols(
                        currencies = currenciesResult,
                        selectedCurrency = baseCurrency,
                    )
                ),
            )
            loadRates()
        }
    }

    private fun loadRates() {
        if (ratesJob?.isActive == true) {
            ratesJob?.cancel()
        }

        ratesJob = viewModelScope.launch(ratesExceptionHandler) {
            _stateUI.value = _stateUI.value.copy(
                ratesUIState = UIState.Loading,
            )
            val result = dataSource.getRates(baseCurrency)

            _stateUI.value = stateUI.value.copy(
                ratesUIState = UIState.Success(
                    CurrenciesContract.Rates(
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

    fun onReloadCurrenciesClicked() {
        loadCurrencies()
    }

    fun onReloadRatesClicked() {
        loadRates()
    }
}