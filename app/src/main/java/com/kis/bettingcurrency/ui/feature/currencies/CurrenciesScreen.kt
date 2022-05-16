package com.kis.bettingcurrency.ui.feature.currencies

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kis.bettingcurrency.R
import com.kis.bettingcurrency.model.Currency
import com.kis.bettingcurrency.model.CurrencyRate
import com.kis.bettingcurrency.ui.NavigationKeys.Params.SORT_STRATEGY
import com.kis.bettingcurrency.ui.NavigationKeys.Route.CURRENCIES_FILTER
import com.kis.bettingcurrency.ui.UIState
import java.math.BigDecimal

@Composable
fun CurrenciesScreenRoute(
    navController: NavController,
    viewModel: CurrenciesViewModel = hiltViewModel(),
) {
    val screenState: CurrenciesContract by viewModel.stateUI.collectAsState()

    CurrenciesScreen(
        symbolsUIState = screenState.symbolsUIState,
        ratesUIState = screenState.ratesUIState,
        onlyFavourite = screenState.onlyFavourite,
        onClickFavouriteIcon = viewModel::onClickFavouriteIcon,
        onSelectedCurrency = viewModel::onSelectedCurrency,
        onReloadCurrenciesClicked = viewModel::onReloadCurrenciesClicked,
        onReloadRatesClicked = viewModel::onReloadRatesClicked,
        onBottomBarClick = viewModel::onBottomBarClick,
        onFiltersClick = { navController.navigate("$CURRENCIES_FILTER/$SORT_STRATEGY=${screenState.sortRateStrategy}") }
    )
}

@Composable
fun CurrenciesScreen(
    symbolsUIState: UIState<CurrenciesContract.Symbols>,
    ratesUIState: UIState<CurrenciesContract.Rates>,
    onlyFavourite: Boolean,
    onClickFavouriteIcon: (CurrencyRate) -> Unit,
    onSelectedCurrency: (Currency) -> Unit,
    onReloadCurrenciesClicked: () -> Unit,
    onReloadRatesClicked: () -> Unit,
    onBottomBarClick: (Boolean) -> Unit,
    onFiltersClick: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            CurrencyBottomNavigationBar(
                onlyFavourite = onlyFavourite,
                onClick = onBottomBarClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    20.dp,
                    20.dp,
                    20.dp,
                    innerPadding.calculateBottomPadding() + 5.dp
                )
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
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 20.dp)
                            ) { onFiltersClick.invoke() }
                    )
                }
            }
            if (ratesUIState is UIState.Success) {
                CurrenciesList(
                    currencies = ratesUIState.data.currencies,
                    onlyFavourite = onlyFavourite,
                    onClickFavouriteIcon = onClickFavouriteIcon,
                )
            }
            if (ratesUIState is UIState.Loading || symbolsUIState is UIState.Loading) {
                CircularProgressIndicator()
            }

            if (symbolsUIState is UIState.Error) {
                CurrenciesSnackBar(
                    symbolsUIState.message,
                    onReloadCurrenciesClicked,
                )
            } else if (ratesUIState is UIState.Error) {
                CurrenciesSnackBar(
                    ratesUIState.message,
                    onReloadRatesClicked,
                )
            }
        }
    }
}

@Composable
fun CurrenciesSnackBar(
    text: String?,
    onReloadClick: () -> Unit,
) {
    Snackbar(
        action = {
            Button(onClick = onReloadClick) {
                Text("Reload")
            }
        },
    ) {
        Text(text = text ?: stringResource(R.string.unknown_error))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrenciesList(
    currencies: List<CurrencyRate>,
    onlyFavourite: Boolean,
    onClickFavouriteIcon: (CurrencyRate) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 8.dp),
    ) {
        items(currencies, key = { rate -> rate.currency.ISO }) { item ->
            if (!onlyFavourite || item.isFavourite) {
                RateItemRow(
                    rateItem = item,
                    onClickFavouriteIcon = onClickFavouriteIcon,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}

@Composable
fun RateItemRow(
    rateItem: CurrencyRate,
    onClickFavouriteIcon: (CurrencyRate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
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
                ) { onClickFavouriteIcon.invoke(rateItem) },
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
                .clickable { expanded = !expanded }
                .width(with(LocalDensity.current) { rowSize.width.toDp() })
                .border(
                    width = 1.dp,
                    color = Color.Black,
                )
                .requiredHeightIn(max = 200.dp),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSelectedItem(item)
                    },
                ) {
                    Text(
                        text = item.ISO,
                        fontWeight = if (item == selectedItem) {
                            FontWeight.Bold
                        } else {
                            null
                        },
                    )
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
                return@with CurrenciesContract.Symbols(
                    this,
                    this.first(),
                )
            }
        ),
        ratesUIState = UIState.Error(),
        false,
        {},
        {},
        {},
        {},
        {},
        { "sadsad" },
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

@Composable
fun CurrencyBottomNavigationBar(
    onlyFavourite: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val items = listOf(
        BottomBarItem.All,
        BottomBarItem.Favourites,
    )
    BottomNavigation(
        contentColor = Color.White,
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxHeight(0.4f)
                    )
                },
                label = { Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = item.onlyFavourite == onlyFavourite,
                onClick = {
                    onClick.invoke(item.onlyFavourite)
                },
            )
        }
    }
}

sealed class BottomBarItem(var onlyFavourite: Boolean, var icon: Int, var title: String) {
    object All :
        BottomBarItem(false, R.drawable.ic_coins_solid, "Home")

    object Favourites :
        BottomBarItem(
            true,
            R.drawable.ic_heart_solid,
            "Favourites"
        )
}

