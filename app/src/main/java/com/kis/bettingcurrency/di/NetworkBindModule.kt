package com.kis.bettingcurrency.di

import com.kis.bettingcurrency.data.repository.CurrencyRepository
import com.kis.bettingcurrency.data.repository.CurrencyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck

@Module
@DisableInstallInCheck
interface NetworkBindModule {

    @Binds
    fun CurrencyRepositoryImpl.bindEmployeeAuthSettingsRepository(): CurrencyRepository

}