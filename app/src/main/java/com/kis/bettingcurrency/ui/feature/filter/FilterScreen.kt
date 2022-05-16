package com.kis.bettingcurrency.ui.feature.filter

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
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kis.bettingcurrency.R
import com.kis.bettingcurrency.core.SortRateStrategy
import com.kis.bettingcurrency.core.viewModel
import com.kis.bettingcurrency.ui.feature.currencies.CurrenciesViewModel

@Composable
fun FilterScreenRoute(
    navController: NavController,
    viewModel: FilterViewModel = hiltViewModel(),
) {
    val parenViewModel: CurrenciesViewModel? = navController.previousBackStackEntry.viewModel()

    val stateUi = viewModel.stateUI.collectAsState()
    FilterScreen(
        stateUi.value,
        viewModel::onSelectedStrategy,
        {
            parenViewModel?.sortRateStrategy = stateUi.value.selectedStrategy
            navController.popBackStack()
        },
        navController::popBackStack,
    )
}

@Composable
fun FilterScreen(
    filterContract: FilterContract,
    onSelectedStrategy: (SortRateStrategy) -> Unit,
    onApply: () -> Unit,
    onCancel: () -> Unit,
) {

    Scaffold { innerPadding ->
        RadioButtonList(
            strategies = filterContract.strategies,
            selectedStrategy = filterContract.selectedStrategy,
            onSelectedStrategy = onSelectedStrategy,
            onApply = onApply,
            onCancel = onCancel,
            innerPadding = innerPadding,
        )
    }
}

@Composable
fun RadioButtonList(
    strategies: List<SortRateStrategy>,
    selectedStrategy: SortRateStrategy,
    onSelectedStrategy: (SortRateStrategy) -> Unit,
    onApply: () -> Unit,
    onCancel: () -> Unit,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                20.dp,
                20.dp,
                20.dp,
                innerPadding.calculateBottomPadding(),
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.sort), fontSize = 20.sp)
        Spacer(modifier = Modifier.size(16.dp))
        strategies.forEach { strategy ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onSelectedStrategy.invoke(strategy) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                RadioButton(
                    selected = strategy == selectedStrategy,
                    onClick = null,
                )
                Text(text = strategy.toString())
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = onCancel) {
                Text(text = stringResource(id = R.string.cancel))
            }
            Button(onClick = onApply) {
                Text(text = stringResource(id = R.string.apply))
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun FilterScreenPreview() {
    FilterScreen(
        FilterContract(),
        {},
        {},
        {},
    )
}