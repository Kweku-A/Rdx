package com.kweku.armah.rdx.ui.screens.onboarding

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.OnboardingData
import com.kweku.armah.rdx.ui.theme.RdxTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TermsOfServiceScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun should_navigate_to_previous() {

        val fakeData = OnboardingData()
        var isNavigating = false

        composeTestRule.setContent {
            RdxTheme {
                TermsOfServiceScreen(
                    uiData = fakeData,
                    onPreviousClick = { isNavigating = true },
                    onNextClick = { },
                    onTermsCheckChange = {})
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.previous
            )
        ).performClick()

        assertTrue(isNavigating)
    }

    @Test
    fun should_disable_next_when_terms_not_agreed() {
        val fakeData = OnboardingData(isTermsAgreed = false)
        var isEnabled = false

        composeTestRule.setContent {
            RdxTheme {
                TermsOfServiceScreen(
                    uiData = fakeData,
                    onPreviousClick = {},
                    onNextClick = { isEnabled = true },
                    onTermsCheckChange = {}
                )
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.next
            )
        ).performClick()

        assertFalse(isEnabled)
    }

    @Test
    fun should_enable_next_when_terms_agreed() {
        var isEnabled = false

        composeTestRule.setContent {
            var state by remember { mutableStateOf(OnboardingData(isTermsAgreed = false)) }
            RdxTheme {
                TermsOfServiceScreen(
                    uiData = state,
                    onPreviousClick = {},
                    onNextClick = { isEnabled = true },
                    onTermsCheckChange = {
                        state = state.copy(isTermsAgreed = it)
                    })
            }
        }

        composeTestRule.onNodeWithContentDescription(
            "checkbox"
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.next
            )
        ).performClick()

        assertTrue(isEnabled)
    }
}