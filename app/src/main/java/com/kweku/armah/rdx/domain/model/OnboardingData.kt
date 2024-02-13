package com.kweku.armah.rdx.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class OnboardingData(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isPhoneNumberValid: Boolean = true,
    val email: String = "",
    val isEmailValid: Boolean = true,
    val password: String = "",
    val pin: String = "",
    val confirmPin: String = "",
    val isTermsAgreed: Boolean = false,
    val onFinishNavigationRoute: String = ""
) : Parcelable