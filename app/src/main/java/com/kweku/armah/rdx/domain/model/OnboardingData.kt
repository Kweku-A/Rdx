package com.kweku.armah.rdx.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class for holding onboarding screen state
@Parcelize
data class OnboardingData(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isPhoneNumberValid: Boolean = false,
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: String = "",
    val pin: String = "",
    val confirmPin: String = "",
    val isPinValid: Boolean = true,
    val isTermsAgreed: Boolean = false,
    val onFinishNavigationRoute: String = ""
) : Parcelable