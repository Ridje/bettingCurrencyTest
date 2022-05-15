package com.kis.bettingcurrency.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kis.bettingcurrency.data.network.CurrencyApi
import com.kis.bettingcurrency.data.network.addProjectInterceptors
import com.kis.bettingcurrency.data.repository.CurrencyRepository
import com.kis.bettingcurrency.data.repository.CurrencyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(
    includes = [NetworkModule.NetworkBindModule::class]
)
@DisableInstallInCheck
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addProjectInterceptors().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .baseUrl(CurrencyApi.API_BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(
        retrofit: Retrofit,
    ): CurrencyApi {
        return retrofit.create(CurrencyApi::class.java)
    }

    @Module
    @DisableInstallInCheck
    interface NetworkBindModule {
        @Binds
        fun CurrencyRepositoryImpl.bindCurrencyRepository(): CurrencyRepository
    }
}