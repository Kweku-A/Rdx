
package com.kweku.armah.rdx.ui.screens.onboarding

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.OnboardingData
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConfirmPinScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun getString(id: Int): String {
        return ApplicationProvider.getApplicationContext<Context>().getString(id)
    }

    @Test
    fun `ConfirmPinScreen should display correctly`() {
        // Given
        val onPreviousClick: () -> Unit = mockk(relaxed = true)
        val onFinishClick: () -> Unit = mockk(relaxed = true)
        val onPinChange: (String) -> Unit = mockk(relaxed = true)
        val uiData = OnboardingData(confirmPin = "")

        // When
        composeTestRule.setContent {
            ConfirmPinScreen(
                uiData = uiData,
                onPreviousClick = onPreviousClick,
                onFinishClick = onFinishClick,
                onPinChange = onPinChange
            )
        }

        // Then
        composeTestRule.onNodeWithText(getString(R.string.new_pin)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.confirm_pin)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.previous)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.finish)).assertIsDisplayed()
    }

    @Test
    fun `Finish button should be disabled when pin is empty`() {
        // Given
        val onPreviousClick: () -> Unit = mockk(relaxed = true)
        val onFinishClick: () -> Unit = mockk(relaxed = true)
        val onPinChange: (String) -> Unit = mockk(relaxed = true)
        val uiData = OnboardingData(confirmPin = "")

        // When
        composeTestRule.setContent {
            ConfirmPinScreen(
                uiData = uiData,
                onPreviousClick = onPreviousClick,
                onFinishClick = onFinishClick,
                onPinChange = onPinChange
            )
        }

        // Then
        composeTestRule.onNodeWithText(getString(R.string.finish)).assertIsNotEnabled()
    }

    @Test
    fun `Finish button should be enabled when pin is not empty`() {
        // Given
        val onPreviousClick: () -> Unit = mockk(relaxed = true)
        val onFinishClick: () -> Unit = mockk(relaxed = true)
        val onPinChange: (String) -> Unit = mockk(relaxed = true)
        val uiData = OnboardingData(confirmPin = "1234")

        // When
        composeTestRule.setContent {
            ConfirmPinScreen(
                uiData = uiData,
                onPreviousClick = onPreviousClick,
                onFinishClick = onFinishClick,
                onPinChange = onPinChange
            )
        }

        // Then
        composeTestRule.onNodeWithText(getString(R.string.finish)).assertIsEnabled()
    }

    @Test
    fun `Clicking Previous button should call onPreviousClick`() {
        // Given
        val onPreviousClick: () -> Unit = mockk(relaxed = true)
        val onFinishClick: () -> Unit = mockk(relaxed = true)
        val onPinChange: (String) -> Unit = mockk(relaxed = true)
        val uiData = OnboardingData()

        // When
        composeTestRule.setContent {
            ConfirmPinScreen(
                uiData = uiData,
                onPreviousClick = onPreviousClick,
                onFinishClick = onFinishClick,
                onPinChange = onPinChange
            )
        }
        composeTestRule.onNodeWithText(getString(R.string.previous)).performClick()

        // Then
        verify { onPreviousClick() }
    }

    @Test
    fun `Clicking Finish button should call onFinishClick`() {
        // Given
        val onPreviousClick: () -> Unit = mockk(relaxed = true)
        val onFinishClick: () -> Unit = mockk(relaxed = true)
        val onPinChange: (String) -> Unit = mockk(relaxed = true)
        val uiData = OnboardingData(confirmPin = "1234")

        // When
        composeTestRule.setContent {
            ConfirmPinScreen(
                uiData = uiData,
                onPreviousClick = onPreviousClick,
                onFinishClick = onFinishClick,
                onPinChange = onPinChange
            )
        }
        composeTestRule.onNodeWithText(getString(R.string.finish)).performClick()

        // Then
        verify { onFinishClick() }
    }
}
