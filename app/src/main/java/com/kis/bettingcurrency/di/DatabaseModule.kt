package com.kis.bettingcurrency.di

import android.content.Context
import androidx.room.Room
import com.kis.bettingcurrency.data.database.BettingCurrencyDatabase
import com.kis.bettingcurrency.data.database.BettingCurrencyDatabase.Companion.DATABASE_NAME
import com.kis.bettingcurrency.data.database.entity.FavouriteCurrencyDao
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@DisableInstallInCheck
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): BettingCurrencyDatabase {
        return Room
            .databaseBuilder(
                context,
                BettingCurrencyDatabase::class.java,
                DATABASE_NAME,
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideFavouriteCurrencyDao(bettingCurrencyDatabase: BettingCurrencyDatabase): FavouriteCurrencyDao {
        return bettingCurrencyDatabase.favouriteCurrencyDao()
    }

}