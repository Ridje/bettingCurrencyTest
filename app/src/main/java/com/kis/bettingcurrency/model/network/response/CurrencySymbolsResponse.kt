package com.kis.bettingcurrency.model.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CurrencySymbolsResponse(
    val success: Boolean,
    val symbols: Map<String, String>,
)
