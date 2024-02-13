package com.kweku.armah.rdx.ui.screens.destinations

sealed class MainScreenDestinations {
    data object Main : MainScreenDestinations()
    data object Onboarding : MainScreenDestinations()
}