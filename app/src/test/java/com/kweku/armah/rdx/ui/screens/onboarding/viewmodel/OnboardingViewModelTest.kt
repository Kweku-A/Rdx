package com.kweku.armah.rdx.ui.screens.onboarding.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.kweku.armah.rdx.data.AppPreferenceDataStore
import com.kweku.armah.rdx.data.PreferenceKeys
import com.kweku.armah.rdx.domain.model.OnboardingData
import com.kweku.armah.rdx.domain.util.IsValidEmail
import com.kweku.armah.rdx.domain.util.IsValidPhoneNumber
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private lateinit var viewModel: OnboardingViewModel
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val isValidEmail: IsValidEmail = mockk()
    private val isValidPhoneNumber: IsValidPhoneNumber = mockk()
    private val appPreferenceDataStore: AppPreferenceDataStore = mockk(relaxed = true)
    private val json: Json = Json

    private val testDispatcher = StandardTestDispatcher()
    private val onboardingState = MutableStateFlow(OnboardingData(isEmailValid = false, isPhoneNumberValid = false))

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { savedStateHandle.getStateFlow("onboardingState", any<OnboardingData>()) } returns onboardingState

        val slot = slot<OnboardingData>()
        every { savedStateHandle.set("onboardingState", capture(slot)) } answers {
            onboardingState.value = slot.captured
        }

        viewModel = OnboardingViewModel(
            savedStateHandle,
            isValidEmail,
            isValidPhoneNumber,
            appPreferenceDataStore,
            json
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onTermsAgreed updates state`() = runTest {
        viewModel.uiState.test {
            var state = awaitItem()
            assertFalse(state.isTermsAgreed)

            viewModel.onTermsAgreed(true)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertTrue(state.isTermsAgreed)
        }
    }

    @Test
    fun `onFirstNameChange updates state`() = runTest {
        val firstName = "John"
        viewModel.uiState.test {
            var state = awaitItem()
            assertEquals("", state.firstName)

            viewModel.onFirstNameChange(firstName)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertEquals(firstName, state.firstName)
        }
    }

    @Test
    fun `onLastNameChange updates state`() = runTest {
        val lastName = "Doe"
        viewModel.uiState.test {
            var state = awaitItem()
            assertEquals("", state.lastName)

            viewModel.onLastNameChange(lastName)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertEquals(lastName, state.lastName)
        }
    }

    @Test
    fun `onEmailNameChange updates state`() = runTest {
        val email = "test@example.com"
        every { isValidEmail(email) } returns true

        viewModel.uiState.test {
            var state = awaitItem()
            assertEquals("", state.email)
            assertFalse(state.isEmailValid)

            viewModel.onEmailNameChange(email)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertEquals(email, state.email)
            assertTrue(state.isEmailValid)
        }
    }

    @Test
    fun `onPasswordChange updates state`() = runTest {
        val password = "password"
        viewModel.uiState.test {
            var state = awaitItem()
            assertEquals("", state.password)

            viewModel.onPasswordChange(password)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertEquals(password, state.password)
        }
    }

    @Test
    fun `onPhoneNumberChange updates state`() = runTest {
        val phoneNumber = "1234567890"
        every { isValidPhoneNumber(phoneNumber) } returns true

        viewModel.uiState.test {
            var state = awaitItem()
            assertEquals("", state.phoneNumber)
            assertFalse(state.isPhoneNumberValid)

            viewModel.onPhoneNumberChange(phoneNumber)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertEquals(phoneNumber, state.phoneNumber)
            assertTrue(state.isPhoneNumberValid)
        }
    }

    @Test
    fun `onPinChange updates state`() = runTest {
        val pin = "123456"
        viewModel.uiState.test {
            var state = awaitItem()
            assertEquals("", state.pin)

            viewModel.onPinChange(pin)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertEquals(pin, state.pin)
        }
    }

    @Test
    fun `onConfirmPin updates state`() = runTest {
        val pin = "123456"
        viewModel.uiState.test {
            var state = awaitItem()
            assertEquals("", state.confirmPin)

            viewModel.onConfirmPin(pin)
            testDispatcher.scheduler.advanceUntilIdle()
            state = awaitItem()
            assertEquals(pin, state.confirmPin)
        }
    }

    @Test
    fun `onFinishClicked with matching pins saves user info and navigates`() = runTest {
        val pin = "123456"
        onboardingState.value = OnboardingData(pin = pin, confirmPin = pin)

        viewModel.onFinishClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { appPreferenceDataStore.updateDataStore(PreferenceKeys.userInfo, any()) }

        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals("Main", finalState.onFinishNavigationRoute)
        }
    }

    @Test
    fun `onFinishClicked with non-matching pins sets error`() = runTest {
        val onboardingData = OnboardingData(pin = "123456", confirmPin = "654321")
        onboardingState.value = onboardingData

        viewModel.onFinishClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { appPreferenceDataStore.updateDataStore(PreferenceKeys.userInfo, any()) }
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertFalse(finalState.isPinValid)
        }
    }
}
