package com.kweku.armah.rdx.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class) // Use the standard AndroidJUnit4 runner
class AppPreferenceDataStoreImplTest {

    private lateinit var context: Context
    private lateinit var appPreferenceDataStore: AppPreferenceDataStoreImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Get a real, working Context from Robolectric
        context = ApplicationProvider.getApplicationContext()
        // Initialize the class under test with the real context
        appPreferenceDataStore = AppPreferenceDataStoreImpl(context, testDispatcher)
    }

    @After
    fun tearDown() = runTest {
        // Clear all preferences after each test to ensure test isolation
        context.dataStore.edit { it.clear() }
    }

    @Test
    fun `updateDataStore and getDataStore should save and retrieve a string value correctly`() =
        runTest(testDispatcher) {
            // Arrange
            val key = stringPreferencesKey("test_string")
            val expectedValue = "hello world"

            // Act: Save the value
            appPreferenceDataStore.updateDataStore(key, expectedValue)

            // Act: Retrieve the value
            val resultFlow = appPreferenceDataStore.getDataStore(key)
            val actualValue = resultFlow.first()

            // Assert
            assertEquals(
                "The retrieved value should match the saved value",
                expectedValue,
                actualValue
            )
        }

    @Test
    fun `getDataStore should return null if key does not exist`() = runTest(testDispatcher) {
        // Arrange
        val key = stringPreferencesKey("non_existent_key")

        // Act
        val resultFlow = appPreferenceDataStore.getDataStore(key)
        val actualValue = resultFlow.first()

        // Assert
        assertNull("The value should be null for a non-existent key", actualValue)
    }

    @Test
    fun `clear should remove all preferences`() = runTest(testDispatcher) {
        // Arrange: Save a value first
        val key = stringPreferencesKey("test_string_to_clear")
        val value = "some data"
        appPreferenceDataStore.updateDataStore(key, value)

        // Sanity check to ensure it was saved
        assertEquals(value, appPreferenceDataStore.getDataStore(key).first())

        // Act: Clear all preferences
        appPreferenceDataStore.clear()

        // Assert: The value should now be null
        assertNull(
            "The value should be null after clearing",
            appPreferenceDataStore.getDataStore(key).first()
        )
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `updateDataStore should throw exception for unsupported type`() = runTest(testDispatcher) {
        // Arrange
        data class UnsupportedType(val name: String)
        // We can't create a real Preferences.Key for a custom type, so mocking the key is still appropriate here.
        val key = mockk<androidx.datastore.preferences.core.Preferences.Key<UnsupportedType>>()
        val value = UnsupportedType("test")

        // Act
        appPreferenceDataStore.updateDataStore(key, value)

        // Assert: The test passes if the expected exception is thrown
    }
}
