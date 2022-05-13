package com.kis.bettingcurrency.ui.feature.currencies

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.kis.bettingcurrency.R
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import com.kis.bettingcurrency.ui.UIState
import java.math.BigDecimal

@Composable
fun CurrenciesScreenRoute(
    viewModel: CurrenciesViewModel,
) {

    val screenState: CurrenciesContract by viewModel.stateUI.collectAsState()

    CurrenciesScreen(
        symbolsUIState = screenState.symbolsUIState,
        ratesUIState = screenState.ratesUIState,
        onClickFavouriteIcon = viewModel::onClickFavouriteIcon,
        onSelectedCurrency = viewModel::onSelectedCurrency,
    )
}

@Composable
fun CurrenciesScreen(
    symbolsUIState: UIState<Symbols>,
    ratesUIState: UIState<Rates>,
    onClickFavouriteIcon: (CurrencyRate) -> Unit,
    onSelectedCurrency: (Currency) -> Unit,
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (symbolsUIState is UIState.Success) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BaseCurrencyDropdownList(
                        items = symbolsUIState.data.currencies,
                        selectedItem = symbolsUIState.data.selectedCurrency,
                        onSelectedItem = onSelectedCurrency,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter_solid),
                        contentDescription = "sort and filters",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            if (ratesUIState is UIState.Success) {
                CurrenciesList(
                    currencies = ratesUIState.data.currencies,
                    onClickFavouriteIcon = onClickFavouriteIcon,
                )
            }
            if (ratesUIState is UIState.Loading || symbolsUIState is UIState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun CurrenciesList(
    currencies: List<CurrencyRate>,
    onClickFavouriteIcon: (CurrencyRate) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 8.dp)
    ) {
        items(currencies) { item ->
            RateItemRow(
                rateItem = item,
                onClickFavouriteIcon = onClickFavouriteIcon,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RateItemRow(
    rateItem: CurrencyRate,
    onClickFavouriteIcon: (CurrencyRate) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = rateItem.currency.ISO)
        Text(text = rateItem.rate.toPlainString())

        val tint by animateColorAsState(
            if (rateItem.isFavourite) Color.Red else Color.Gray
        )
        Icon(
            painter = if (rateItem.isFavourite) {
                painterResource(id = R.drawable.ic_heart_solid)
            } else {
                painterResource(id = R.drawable.ic_heart_regular)
            },
            contentDescription = "Expand row icon",
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClickFavouriteIcon.invoke(rateItem) }
                .clipToBounds(),
            tint = tint,
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
    var rowSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier = modifier.onGloballyPositioned { layoutCoordinates ->
            rowSize = layoutCoordinates.size.toSize()
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { expanded = !expanded },
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                )
                .padding(8.dp)
        ) {
            Text(
                text = selectedItem.ISO,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentHeight()
                .clickable { expanded = !expanded }
                .width(with(LocalDensity.current) { rowSize.width.toDp() })
                .border(
                    width = 1.dp,
                    color = Color.Black,
                )
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSelectedItem(item)
                    },
                ) {
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
        symbolsUIState = UIState.Success(
            with(listOfSymbols(3)) {
                return@with Symbols(
                    this,
                    this.first(),
                )
            }
        ),
        ratesUIState = UIState.Success(
            data = Rates(listOfCurrencyRate(30))
        ),
        {},
        {},
    )
}

private fun listOfCurrencyRate(count: Int): List<CurrencyRate> {
    val list: MutableList<CurrencyRate> = mutableListOf()

    for (i in 1..count) {
        list.add(
            CurrencyRate(Currency("VAL$i"), BigDecimal(i), i % 2 == 0)
        )
    }
    list.sortBy { it.rate }
    return list
}

private fun listOfSymbols(count: Int): List<Currency> {
    val list: MutableList<Currency> = mutableListOf()

    for (i in 1..count) {
        list.add(
            Currency("VAL$i"),
        )
    }
    return list
}

