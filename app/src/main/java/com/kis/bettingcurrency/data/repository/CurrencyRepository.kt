package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate

interface CurrencyRepository {
    suspend fun getFavouriteRates(baseCurrency: Currency): List<CurrencyRate>
    suspend fun getRates(baseCurrency: Currency): List<CurrencyRate>
    suspend fun getCurrencies(): List<Currency>
    suspend fun addFavourite(currency: Currency)
    suspend fun removeFavourite(currency: Currency)
}