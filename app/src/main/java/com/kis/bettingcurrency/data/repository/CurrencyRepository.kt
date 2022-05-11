package com.kis.bettingcurrency.data.repository

interface CurrencyRepository {
    suspend fun getFavouriteRates(baseCurrency: String)
    suspend fun getRates(baseCurrency: String)
    suspend fun getCurrencies()
    suspend fun addFavourite(currency: String)
    suspend fun removeFavourite(currency: String)
}