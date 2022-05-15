package com.kis.bettingcurrency.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [DatabaseModule::class, NetworkModule::class, UtilityModule::class]
)
@InstallIn(SingletonComponent::class)
object AppModule {
}