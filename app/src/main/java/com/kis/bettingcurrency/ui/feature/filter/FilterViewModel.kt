package com.kis.bettingcurrency.ui.feature.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kis.bettingcurrency.core.SortRateStrategy
import com.kis.bettingcurrency.ui.NavigationKeys.Params.SORT_STRATEGY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var selectedSortStrategy: SortRateStrategy = SortRateStrategy.valueOf(
        savedStateHandle.get<String>(SORT_STRATEGY) ?: SortRateStrategy.ISO_ASC.name
    )
    set(value) {
        field = value
        _stateUI.value = _stateUI.value.copy(
            selectedStrategy = value
        )
    }
    private val _stateUI: MutableStateFlow<FilterContract> = MutableStateFlow(
        FilterContract(
            selectedStrategy = selectedSortStrategy
        )
    )
    val stateUI: StateFlow<FilterContract>
        get() {
            return _stateUI
        }

    fun onSelectedStrategy(selectedSortStrategy:SortRateStrategy) {
        this.selectedSortStrategy = selectedSortStrategy
    }

}