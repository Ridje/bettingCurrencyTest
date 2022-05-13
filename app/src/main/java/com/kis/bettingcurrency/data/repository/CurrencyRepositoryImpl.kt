package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.data.database.entity.FavouriteCurrency
import com.kis.bettingcurrency.data.database.entity.FavouriteCurrencyDao
import com.kis.bettingcurrency.data.network.CurrencyApi
import com.kis.bettingcurrency.data.network.response.CurrencyRateResponse
import com.kis.bettingcurrency.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val favouriteDao: FavouriteCurrencyDao,
    private val ratesMapper: CurrencyRatesMapper,
) : CurrencyRepository {
    override suspend fun getFavouriteRates(baseCurrency: Currency) =
        withContext(Dispatchers.IO) {
            val ratesJob = async {
                return@async currencyRates(50)
            }
            val favouriteJob = async {
                return@async favouriteDao.getAll()
            }
            return@withContext ratesMapper.mapRates(
                ratesJob.await(),
                favouriteJob.await()
            ).filter { !it.isFavourite }
        }

    override suspend fun getRates(baseCurrency: Currency) =
        withContext(Dispatchers.IO) {
            val ratesJob = async {
                return@async currencyRates(50)
            }
            val favouriteJob = async {
                return@async favouriteDao.getAll()
            }
            return@withContext ratesMapper.mapRates(ratesJob.await(), favouriteJob.await())
        }

    override suspend fun getCurrencies(): List<Currency> {
        return listOfSymbols(5)
    }

    override suspend fun addFavourite(currency: Currency) = withContext(Dispatchers.IO) {
        favouriteDao.add(FavouriteCurrency(currency.ISO))
    }

    override suspend fun removeFavourite(currency: Currency) = withContext(Dispatchers.IO) {
        favouriteDao.remove(FavouriteCurrency(currency.ISO))
    }
}

private fun currencyRates(count: Int): CurrencyRateResponse {

    return CurrencyRateResponse(
        base = "test",
        date = Clock.System.now(),
        rates = generateSequence(0) { it + 1 }.take(count)
            .associateBy(
                { "VAL${it}" },
                { BigDecimal(it) },
            ),
        success = true,
        timestamp = Clock.System.now(),
    )
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