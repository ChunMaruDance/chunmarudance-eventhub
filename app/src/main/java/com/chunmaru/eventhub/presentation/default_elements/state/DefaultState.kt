package com.chunmaru.eventhub.presentation.default_elements.state

sealed class ScreenState<T> {

    class Initial<T> : ScreenState<T>()

    class Pending<T> : ScreenState<T>()

    data class Success<T>(
        val data: T
    ) : ScreenState<T>()

}