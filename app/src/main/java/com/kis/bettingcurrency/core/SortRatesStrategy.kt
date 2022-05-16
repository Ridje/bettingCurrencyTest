package com.kis.bettingcurrency.core

import com.kis.bettingcurrency.model.CurrencyRate
import javax.inject.Inject

interface SortRatesStrategy {
    fun sort(list: List<CurrencyRate>): List<CurrencyRate>
}

class ISODescSortStrategy() : SortRatesStrategy {

    override fun sort(list: List<CurrencyRate>): List<CurrencyRate> {
        return list.sortedByDescending { it.currency.ISO }
    }
}

class ISOAscSortStrategy() : SortRatesStrategy {
    override fun sort(list: List<CurrencyRate>): List<CurrencyRate> {
        return list.sortedBy { it.currency.ISO }
    }
}

class RateDescSortStrategy() : SortRatesStrategy {
    override fun sort(list: List<CurrencyRate>): List<CurrencyRate> {
        return list.sortedByDescending { it.rate }
    }

}

class RateAscSortStrategy() : SortRatesStrategy {
    override fun sort(list: List<CurrencyRate>): List<CurrencyRate> {
        return list.sortedBy { it.rate }
    }
}

class SortStrategyFactory @Inject constructor() {
    fun getStrategy(strategy: SortRateStrategy): SortRatesStrategy {
        return when (strategy) {
            SortRateStrategy.ISO_DESC -> ISODescSortStrategy()
            SortRateStrategy.ISO_ASC -> ISOAscSortStrategy()
            SortRateStrategy.RATE_ASC -> RateAscSortStrategy()
            SortRateStrategy.RATE_DESC -> RateDescSortStrategy()
        }
    }
}

enum class SortRateStrategy {
    ISO_DESC,
    ISO_ASC,
    RATE_DESC,
    RATE_ASC,
}