package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.data.database.entity.FavouriteCurrency
import com.kis.bettingcurrency.data.network.response.CurrencyRateResponse
import com.kis.bettingcurrency.data.network.response.CurrencySymbolsResponse
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import javax.inject.Inject

class CurrenciesMapper @Inject constructor() {
    fun mapRates(
        rates: CurrencyRateResponse,
        favouriteCurrencies: List<FavouriteCurrency>
    ): List<CurrencyRate> {
        return rates.rates.map { rate ->
            CurrencyRate(
                Currency(rate.key),
                rate.value,
                favouriteCurrencies.find { favouriteCurrency -> favouriteCurrency.ISO == rate.key } != null
            )
        }
    }

    fun mapFavouriteRates(
        rates: CurrencyRateResponse
    ): List<CurrencyRate> {
        return rates.rates.map { rate ->
            CurrencyRate(
                Currency(rate.key),
                rate.value,
                true,
            )
        }
    }

    fun mapCurrencies(symbolsResponse: CurrencySymbolsResponse): List<Currency> {
        return symbolsResponse.symbols.map { Currency(it.key) }
    }
}