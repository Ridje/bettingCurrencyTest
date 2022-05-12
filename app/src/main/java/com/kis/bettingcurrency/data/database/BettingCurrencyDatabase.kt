package com.kis.bettingcurrency.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kis.bettingcurrency.data.database.entity.FavouriteCurrency
import com.kis.bettingcurrency.data.database.entity.FavouriteCurrencyDao

@Database(
    entities = [FavouriteCurrency::class],
    version = 1,
)
abstract class BettingCurrencyDatabase : RoomDatabase() {
    abstract fun favouriteCurrencyDao(): FavouriteCurrencyDao

    companion object {
        const val DATABASE_NAME = "favourite"
    }
}