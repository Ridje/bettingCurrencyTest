package com.kis.bettingcurrency.ui.feature.currencies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kis.bettingcurrency.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val dataSource: CurrencyRepository,
) : ViewModel() {

    val state by mutableStateOf(
        CurrenciesContract.State(
            currencies = listOf(),
            symbols = listOf(),
        )
    )

    private fun loadRates() {
        viewModelScope.launch {
            dataSource.getRates()
        }
    }
}