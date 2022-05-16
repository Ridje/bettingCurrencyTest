package com.kis.bettingcurrency.data.network

import com.kis.bettingcurrency.data.network.response.CurrencyRateResponse
import com.kis.bettingcurrency.data.network.response.CurrencySymbolsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    companion object {
        const val API_BASE_URL = "https://api.apilayer.com/exchangerates_data/"
    }

    @GET("symbols")
    suspend fun getSymbols(): CurrencySymbolsResponse

    @GET("latest")
    suspend fun getLatest(
        @Query("base")
        base: String?,
    ): CurrencyRateResponse
}