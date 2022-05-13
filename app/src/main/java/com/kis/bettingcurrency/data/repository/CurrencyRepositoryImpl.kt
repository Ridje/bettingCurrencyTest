package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.data.database.BettingCurrencyDatabase
import com.kis.bettingcurrency.data.network.CurrencyApi
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val favouriteDatabase: BettingCurrencyDatabase,
) : CurrencyRepository {
    override suspend fun getFavouriteRates(baseCurrency: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun getRates(baseCurrency: String?): List<CurrencyRate> {
        delay(5000L)
        return listOfCurrencyRate(20)
    }

    override suspend fun getCurrencies(): List<Currency> {
        delay(2500L)
        return listOfSymbols(5)
    }

    override suspend fun addFavourite(currency: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavourite(currency: String) {
        TODO("Not yet implemented")
    }
}

private fun listOfCurrencyRate(count: Int): List<CurrencyRate> {
    val list: MutableList<CurrencyRate> = mutableListOf()

    for (i in 1..count) {
        list.add(
            CurrencyRate(Currency("VAL$i", i % 2 == 0), BigDecimal(i))
        )
    }
    list.sortBy { it.rate }
    return list
}

private fun listOfSymbols(count: Int): List<Currency> {
    val list: MutableList<Currency> = mutableListOf()

    for (i in 1..count) {
        list.add(
            Currency("VAL$i", i % 2 == 0),
        )
    }
    return list
}