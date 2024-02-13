package com.kweku.armah.rdx.domain.wrapper

data class UiState<T>(
    val uiData: T,
    val isLoading: Boolean = true,
)
