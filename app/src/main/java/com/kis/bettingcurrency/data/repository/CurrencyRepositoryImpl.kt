package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.data.database.BettingCurrencyDatabase
import com.kis.bettingcurrency.data.network.CurrencyApi
import kotlinx.coroutines.flow.flow

class CurrencyRepositoryImpl constructor(
    private val currencyApi: CurrencyApi,
    private val favouriteDatabase: BettingCurrencyDatabase,
) : CurrencyRepository {
    override suspend fun getFavouriteRates(baseCurrency: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRates(baseCurrency: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencies() {
        TODO("Not yet implemented")
    }

    override suspend fun addFavourite(currency: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavourite(currency: String) {
        TODO("Not yet implemented")
    }
}