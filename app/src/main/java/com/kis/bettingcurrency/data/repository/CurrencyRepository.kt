package com.kis.bettingcurrency.data.repository

interface CurrencyRepository {
    suspend fun getFavouriteRates(baseCurrency: String? = null)
    suspend fun getRates(baseCurrency: String? = null)
    suspend fun getCurrencies()
    suspend fun addFavourite(currency: String)
    suspend fun removeFavourite(currency: String)
}