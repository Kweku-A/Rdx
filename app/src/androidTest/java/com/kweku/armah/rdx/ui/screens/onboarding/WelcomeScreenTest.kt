package com.kweku.armah.rdx.ui.screens.onboarding

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.ui.theme.RdxTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun should_display_welcome_message() {

        composeTestRule.setContent {
            RdxTheme {
               WelcomeScreen {}
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.welcome_message
            )
        ).assertExists()
    }

    @Test
    fun should_navigate_to_next_screen() {
        var isNavigating = false
        composeTestRule.setContent {
            RdxTheme {
                WelcomeScreen{
                    isNavigating = true
                }
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.next
            )
        ).performClick()

        assertTrue(isNavigating)
    }
}