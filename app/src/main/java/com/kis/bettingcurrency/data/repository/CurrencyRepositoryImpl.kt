package com.kis.bettingcurrency.data.repository

import com.kis.bettingcurrency.data.database.entity.FavouriteCurrency
import com.kis.bettingcurrency.data.database.entity.FavouriteCurrencyDao
import com.kis.bettingcurrency.data.network.CurrencyApi
import com.kis.bettingcurrency.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val favouriteDao: FavouriteCurrencyDao,
    private val currenciesMapper: CurrenciesMapper,
) : CurrencyRepository {

    override suspend fun getRates(baseCurrency: Currency) =
        withContext(Dispatchers.IO) {
            val ratesJob = async {
                return@async currencyApi.getLatest(baseCurrency.ISO)
            }
            val favouriteJob = async {
                return@async favouriteDao.getAll()
            }
            return@withContext currenciesMapper.mapRates(ratesJob.await(), favouriteJob.await())
        }

    override suspend fun getCurrencies() = withContext(Dispatchers.IO) {
        return@withContext currenciesMapper.mapCurrencies(currencyApi.getSymbols())
    }

    override suspend fun addFavourite(currency: Currency) = withContext(Dispatchers.IO) {
        favouriteDao.add(FavouriteCurrency(currency.ISO))
    }

    override suspend fun removeFavourite(currency: Currency) = withContext(Dispatchers.IO) {
        favouriteDao.remove(FavouriteCurrency(currency.ISO))
    }
}
