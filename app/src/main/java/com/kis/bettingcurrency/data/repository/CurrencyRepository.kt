package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate

interface CurrencyRepository {
    suspend fun getFavouriteRates(baseCurrency: String? = null)
    suspend fun getRates(baseCurrency: String? = null): List<CurrencyRate>
    suspend fun getCurrencies(): List<Currency>
    suspend fun addFavourite(currency: String)
    suspend fun removeFavourite(currency: String)
}