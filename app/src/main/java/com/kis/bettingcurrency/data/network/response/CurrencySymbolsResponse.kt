package com.kis.bettingcurrency.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CurrencySymbolsResponse(
    val success: Boolean,
    val symbols: Map<String, String>,
)
