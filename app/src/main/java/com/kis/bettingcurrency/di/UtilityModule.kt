package com.kis.bettingcurrency.di

import android.content.Context
import com.kis.bettingcurrency.core.ResourceProvider
import com.kis.bettingcurrency.core.ResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module(
    includes = [UtilityModule.UtilityBindModule::class]
)
@DisableInstallInCheck
object UtilityModule {

    @Provides
    @Singleton
    fun provideResourceProvider(
        @ApplicationContext
        context: Context,
    ): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Module
    @DisableInstallInCheck
    interface UtilityBindModule {
        @Binds
        fun ResourceProviderImpl.bindResourceProvider(): ResourceProvider
    }
}
