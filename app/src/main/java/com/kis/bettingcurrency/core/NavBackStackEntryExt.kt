package com.kis.bettingcurrency.core

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry?.viewModel(): T? = this?.let {
    viewModel(viewModelStoreOwner = it)
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): T {
    return hiltViewModel(
        viewModelStoreOwner = viewModelStoreOwner,
    )
}