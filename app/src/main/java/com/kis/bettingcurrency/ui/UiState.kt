package com.kis.bettingcurrency.ui

sealed class UIState<out T : Any> {
    data class Success<out T: Any>(val data: T) : UIState<T>()
    object Loading : UIState<Nothing>()
    class Error(val message: String) : UIState<Nothing>()
}