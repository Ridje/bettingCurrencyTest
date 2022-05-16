package com.kis.bettingcurrency.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val ISO: String,
) : Parcelable
