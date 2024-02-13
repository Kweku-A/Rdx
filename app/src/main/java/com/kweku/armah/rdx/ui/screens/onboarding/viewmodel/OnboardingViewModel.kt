package com.kweku.armah.rdx.ui.screens.onboarding.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kweku.armah.rdx.data.AppPreferenceDataStore
import com.kweku.armah.rdx.data.PreferenceKeys
import com.kweku.armah.rdx.domain.model.OnboardingData
import com.kweku.armah.rdx.domain.model.UserInfo
import com.kweku.armah.rdx.domain.util.EmailValidator
import com.kweku.armah.rdx.domain.util.PhoneNumberValidator
import com.kweku.armah.rdx.ui.screens.destinations.MainScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val emailValidator: EmailValidator,
    private val phoneNumberValidator: PhoneNumberValidator,
    private val appPreferenceDataStore: AppPreferenceDataStore,
    private val json: Json
) : ViewModel() {

    var uiState = savedStateHandle.getStateFlow("onboardingState", OnboardingData())

    fun onTermsAgreed(isChecked: Boolean) {
        savedStateHandle["onboardingState"] =
            savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                isTermsAgreed = isChecked
            )
    }

    fun onFirstNameChange(firstName: String) {
        savedStateHandle["onboardingState"] =
            savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                firstName = firstName
            )
    }

    fun onLastNameChange(lastName: String) {
        savedStateHandle["onboardingState"] =
            savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                lastName = lastName
            )
    }

    fun onEmailNameChange(email: String) {
        savedStateHandle["onboardingState"] =
            savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                email = email,
                isEmailValid = emailValidator(email)
            )
    }

    fun onPasswordChange(password: String) {
        savedStateHandle["onboardingState"] =
            savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                password = password
            )
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        savedStateHandle["onboardingState"] =
            savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                phoneNumber = phoneNumber,
                isPhoneNumberValid = phoneNumberValidator(phoneNumber)
            )
    }

    fun onPinChange(pin: String) {
        if (pin.length < 7) {
            savedStateHandle["onboardingState"] =
                savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                    pin = pin
                )
        }
    }

    fun onConfirmPin(pin: String) {
        if (pin.length < 7)
            savedStateHandle["onboardingState"] =
                savedStateHandle.get<OnboardingData>("onboardingState")?.copy(
                    confirmPin = pin
                )
    }

    fun onFinishClicked() {
        val onboardingData = savedStateHandle.get<OnboardingData>("onboardingState")
        onboardingData?.let { uiData ->
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
                        uiData.copy(
                            onFinishNavigationRoute = MainScreenDestinations.Main.toString()
                        )
                }
            }
        }
    }
}