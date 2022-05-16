package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.data.database.entity.FavouriteCurrency
import com.kis.bettingcurrency.data.database.entity.FavouriteCurrencyDao
import com.kis.bettingcurrency.data.network.CurrencyApi
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val favouriteDao: FavouriteCurrencyDao,
    private val currenciesMapper: CurrenciesMapper,
) : CurrencyRepository {
    override suspend fun getFavouriteRates(baseCurrency: Currency) =
        withContext(Dispatchers.IO) {
            val favouriteCurrencies = favouriteDao.getAll()
            return@withContext if (favouriteCurrencies.isEmpty()) {
                listOf()
            } else {
                val latestRates =
                    currencyApi.getLatest(favouriteCurrencies.map { it.ISO }, baseCurrency.ISO)
                currenciesMapper.mapFavouriteRates(latestRates)
            }
        }

    override suspend fun getRates(baseCurrency: Currency) =
        withContext(Dispatchers.IO) {
//            val ratesJob = async {
//                return@async currencyApi.getLatest(null, baseCurrency.ISO)
//            }
//            val favouriteJob = async {
//                return@async favouriteDao.getAll()
//            }
//            return@withContext currenciesMapper.mapRates(ratesJob.await(), favouriteJob.await())
            return@withContext listOfCurrencyRate(30)
        }

    override suspend fun getCurrencies() = withContext(Dispatchers.IO) {
//        return@withContext currenciesMapper.mapCurrencies(currencyApi.getSymbols())
        return@withContext listOfSymbols(30)
    }

    override suspend fun addFavourite(currency: Currency) = withContext(Dispatchers.IO) {
        favouriteDao.add(FavouriteCurrency(currency.ISO))
    }

    override suspend fun removeFavourite(currency: Currency) = withContext(Dispatchers.IO) {
        favouriteDao.remove(FavouriteCurrency(currency.ISO))
    }
}

private fun listOfCurrencyRate(count: Int): List<CurrencyRate> {
    val list: MutableList<CurrencyRate> = mutableListOf()

    for (i in 1..count) {
        list.add(
            CurrencyRate(Currency("VAL$i"), BigDecimal(i), i % 2 == 0)
        )
    }
    list.sortBy { it.rate }
    return list
}

private fun listOfSymbols(count: Int): List<Currency> {
    val list: MutableList<Currency> = mutableListOf()

    for (i in 1..count) {
        list.add(
            Currency("VAL$i"),
        )
    }
    return list
}