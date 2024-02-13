package com.kweku.armah.rdx.ui.screens.destinations

sealed class BaseScreenDestinations {
    data object Main : BaseScreenDestinations()
    data object Onboarding : BaseScreenDestinations()
}