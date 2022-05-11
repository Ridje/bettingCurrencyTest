package com.kis.bettingcurrency.data.network

import com.kis.bettingcurrency.data.network.response.CurrencyRateResponse
import com.kis.bettingcurrency.data.network.response.CurrencySymbolsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    companion object {
        const val API_BASE_URL = "https://api.exchangeratesapi.io/v1/"
    }

    @GET("symbols")
    fun getSymbols(): CurrencySymbolsResponse

    @GET("latest")
    fun getLatest(
        @Query("symbols")
        symbols: List<String>?,

        @Query("base")
        base: String?,
    ): CurrencyRateResponse

}