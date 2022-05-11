package com.kis.bettingcurrency.model.network.response

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRateResponse(
    val base: String,
    val date: Instant,
    val rates: Map<String, String>,
    val success: Boolean,
    val timestamp: Instant,
)
