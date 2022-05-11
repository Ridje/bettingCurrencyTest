package com.kis.bettingcurrency.data.database.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class FavouriteCurrency(
    @PrimaryKey(autoGenerate = false)
    val ISO: String,
)

@Dao
interface FavouriteCurrencyDao {

    @Query("SELECT FavouriteCurrency.ISO FROM FavouriteCurrency")
    fun getAll(): List<FavouriteCurrency>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(favourite: FavouriteCurrency)

    @Delete
    fun remove(vararg favourites: FavouriteCurrency)
}