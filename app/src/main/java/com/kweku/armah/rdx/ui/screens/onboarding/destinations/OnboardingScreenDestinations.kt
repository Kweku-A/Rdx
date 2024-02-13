package com.kweku.armah.rdx.ui.screens.onboarding.destinations

sealed class OnboardingScreenDestinations {
    data object ConfirmPin : OnboardingScreenDestinations()
    data object Credentials : OnboardingScreenDestinations()
    data object NewPin : OnboardingScreenDestinations()
    data object PersonalInfo : OnboardingScreenDestinations()
    data object TermsOfService : OnboardingScreenDestinations()
    data object Welcome : OnboardingScreenDestinations()
}