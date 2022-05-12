package com.kis.bettingcurrency.ui.feature.currencies

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kis.bettingcurrency.R
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import java.math.BigDecimal

@Composable
fun CurrenciesScreen(state: CurrenciesContract.State) {
    Scaffold {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BaseCurrencyDropdownList(
                    items = state.symbols,
                    selectedItem = state.symbols.first(),
                    onSelectedItem = {},
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter_solid),
                    contentDescription = "sort and filters",
                    modifier = Modifier.size(20.dp)
                )
            }
            CurrenciesList(currencies = state.currencies)
        }
    }
}

@Composable
fun CurrenciesList(
    currencies: List<CurrencyRate>
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 8.dp)
    ) {
        items(currencies) { item ->
            RateItemRow(rateItem = item)
        }
    }
}

@Composable
fun RateItemRow(
    rateItem: CurrencyRate
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = rateItem.currency.ISO)
        Text(text = rateItem.rate.toPlainString())
        Icon(
            painter = if (rateItem.currency.isFavourite) {
                painterResource(id = R.drawable.ic_heart_solid)
            } else {
                painterResource(id = R.drawable.ic_heart_regular)
            },
            contentDescription = "Expand row icon",
            modifier = Modifier.size(20.dp),
            tint = if (rateItem.currency.isFavourite) {
                Color.Red
            } else {
                LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            }
        )
    }
}

@Composable
fun BaseCurrencyDropdownList(
    items: List<Currency>,
    selectedItem: Currency,
    onSelectedItem: (Currency) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = selectedItem.ISO,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp),
                )
                .clickable { expanded = !expanded }
                .padding(8.dp),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentHeight()
                .clickable { expanded = !expanded }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onSelectedItem(item)
                }) {
                    Text(text = item.ISO)
                }
            }
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun CurrenciesScreenPreview() {
    CurrenciesScreen(
        state = CurrenciesContract.State(
            listOfCurrencyRate(60),
            listOfSymbols(15),
        )
    )
}

private fun listOfCurrencyRate(count: Int): List<CurrencyRate> {
    val list: MutableList<CurrencyRate> = mutableListOf()

    for (i in 1..count) {
        list.add(
            CurrencyRate(Currency("VAL$i", i % 2 == 0), BigDecimal(i))
        )
    }
    list.sortBy { it.rate }
    return list
}

private fun listOfSymbols(count: Int): List<Currency> {
    val list: MutableList<Currency> = mutableListOf()

    for (i in 1..count) {
        list.add(
            Currency("VAL$i", i % 2 == 0),
        )
    }
    return list
}

