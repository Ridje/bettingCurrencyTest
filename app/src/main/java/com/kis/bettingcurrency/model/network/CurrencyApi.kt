package com.kis.bettingcurrency.model.network

import com.kis.bettingcurrency.model.network.response.CurrencyRateResponse
import com.kis.bettingcurrency.model.network.response.CurrencySymbolsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    interface Service {

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

    companion object {
        const val API_BASE_URL = "https://api.exchangeratesapi.io/v1/"
    }
}