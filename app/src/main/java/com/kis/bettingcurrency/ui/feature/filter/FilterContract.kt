package com.kis.bettingcurrency.ui.feature.filter

import com.kis.bettingcurrency.core.SortRateStrategy

data class FilterContract(
    val strategies: List<SortRateStrategy> = listOf(
        SortRateStrategy.ISO_ASC,
        SortRateStrategy.ISO_DESC,
        SortRateStrategy.RATE_DESC,
        SortRateStrategy.RATE_ASC,
    ),
    val selectedStrategy: SortRateStrategy = strategies.first(),
)
