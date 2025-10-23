package com.kweku.armah.rdx.ui.screens.onboarding.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kweku.armah.rdx.data.AppPreferenceDataStore
import com.kweku.armah.rdx.data.PreferenceKeys
import com.kweku.armah.rdx.domain.model.OnboardingData
import com.kweku.armah.rdx.domain.model.UserInfo
import com.kweku.armah.rdx.domain.util.IsValidEmail
import com.kweku.armah.rdx.domain.util.IsValidPhoneNumber
import com.kweku.armah.rdx.ui.screens.destinations.BaseScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val isValidEmail: IsValidEmail,
    private val isValidPhoneNumber: IsValidPhoneNumber,
    private val appPreferenceDataStore: AppPreferenceDataStore,
    private val json: Json
) : ViewModel() {

    var uiState = savedStateHandle.getStateFlow("onboardingState", OnboardingData())

    fun onTermsAgreed(isChecked: Boolean) {
        savedStateHandle["onboardingState"] = uiState.value.copy(isTermsAgreed = isChecked)
    }

    fun onFirstNameChange(firstName: String) {
        savedStateHandle["onboardingState"] = uiState.value.copy(firstName = firstName)
    }

    fun onLastNameChange(lastName: String) {
        savedStateHandle["onboardingState"] = uiState.value.copy(lastName = lastName)
    }

    fun onEmailNameChange(email: String) {
        savedStateHandle["onboardingState"] =
            uiState.value.copy(email = email, isEmailValid = isValidEmail(email))
    }

    fun onPasswordChange(password: String) {
        savedStateHandle["onboardingState"] = uiState.value.copy(password = password)
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        savedStateHandle["onboardingState"] = uiState.value.copy(
            phoneNumber = phoneNumber,
            isPhoneNumberValid = isValidPhoneNumber(phoneNumber)
        )
    }

    fun onPinChange(pin: String) {
        if (pin.length < 7) {
            savedStateHandle["onboardingState"] = uiState.value.copy(pin = pin)
        }
    }

    fun onConfirmPin(pin: String) {
        if (pin.length < 7)
            savedStateHandle["onboardingState"] =
                uiState.value.copy(confirmPin = pin, isPinValid = true)
    }

    fun onFinishClicked() {
        val uiData = uiState.value
        if (uiData.pin == uiData.confirmPin) {
            viewModelScope.launch {

                val userInfo = UserInfo(
                    firstName = uiData.firstName,
                    lastName = uiData.lastName,
                    email = uiData.email,
                    phoneNumber = uiData.phoneNumber,
                    pin = uiData.pin,
                )

                appPreferenceDataStore.updateDataStore(
                    PreferenceKeys.userInfo,
                    json.encodeToString(userInfo)
                )

                savedStateHandle["onboardingState"] =
                    uiData.copy(onFinishNavigationRoute = BaseScreenDestinations.Main.toString())
            }
        } else {
            savedStateHandle["onboardingState"] = uiState.value.copy(isPinValid = false)
        }
    }
}
