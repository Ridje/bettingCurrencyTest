package com.kis.bettingcurrency.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class CurrencyRate(
    val currency: Currency,
    val rate: BigDecimal,
    val isFavourite: Boolean,
) : Parcelable
