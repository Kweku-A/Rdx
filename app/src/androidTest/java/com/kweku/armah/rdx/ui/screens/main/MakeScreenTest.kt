package com.kweku.armah.rdx.ui.screens.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.UserInfo
import com.kweku.armah.rdx.domain.wrapper.UiState
import com.kweku.armah.rdx.ui.theme.RdxTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MakeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun should_verify_user_info_displayed() {
        val fakeUiState = UiState(
            UserInfo(
                firstName = "a",
                lastName = "b",
                email = "abc@abc.com",
                phoneNumber = "+36305554444"
            ), isLoading = false
        )
        composeTestRule.setContent {
            RdxTheme {
                MainScreen(uiState = fakeUiState, navigateToOnboarding = {}, onLogout = {})
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.welcome_user,
                fakeUiState.uiData.firstName,
                fakeUiState.uiData.lastName
            )
        ).assertExists()
    }

    @Test
    fun should_navigate_to_onboarding_when_no_user_info() {
        val fakeUiState = UiState(UserInfo(), isLoading = false)
        var isNavigateToOnboarding = false
        composeTestRule.setContent {
            RdxTheme {
                MainScreen(uiState = fakeUiState,
                    navigateToOnboarding = { isNavigateToOnboarding = true },
                    onLogout = {})
            }
        }

        assertTrue(isNavigateToOnboarding)
    }

    @Test
    fun should_logout_on_click() {
        val fakeUiState = UiState(
            UserInfo(
                firstName = "a",
                lastName = "b",
                email = "abc@abc.com",
                phoneNumber = "+36305554444"
            ), isLoading = false
        )
        var isLoggingOut = false
        composeTestRule.setContent {
            RdxTheme {
                MainScreen(
                    uiState = fakeUiState,
                    navigateToOnboarding = {},
                    onLogout = { isLoggingOut = true })
            }
        }

        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.logout))
            .performClick()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.logout))
            .performClick()
        assertTrue(isLoggingOut)
    }

    @Test
    fun should_not_logout_on_dialog_cancel() {
        val fakeUiState = UiState(
            UserInfo(
                firstName = "a",
                lastName = "b",
                email = "abc@abc.com",
                phoneNumber = "+36305554444"
            ), isLoading = false
        )
        var isLoggingOut = false
        composeTestRule.setContent {
            RdxTheme {
                MainScreen(
                    uiState = fakeUiState,
                    navigateToOnboarding = {},
                    onLogout = { isLoggingOut = true })
            }
        }

        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.logout))
            .performClick()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.cancel))
            .performClick()
        assertFalse(isLoggingOut)
    }
}