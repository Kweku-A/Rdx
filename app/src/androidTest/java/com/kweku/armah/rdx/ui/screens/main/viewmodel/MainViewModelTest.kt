package com.kweku.armah.rdx.ui.screens.main.viewmodel

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.kweku.armah.rdx.data.FakeAppPreferenceDataStoreImpl
import com.kweku.armah.rdx.data.PreferenceKeys
import com.kweku.armah.rdx.domain.model.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var sut: MainViewModel
    private lateinit var appPreferenceDataStore: FakeAppPreferenceDataStoreImpl
    private lateinit var json: Json

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        appPreferenceDataStore = FakeAppPreferenceDataStoreImpl(appContext)
        json = Json
        sut = MainViewModel(appPreferenceDataStore = appPreferenceDataStore, json = json)
    }

    @Test
    fun should_return_no_user_info_when_not_signed_in() = runTest {
        // check value is not in datastore
        assertTrue(
            appPreferenceDataStore.getDataStore(PreferenceKeys.userInfo).first()?.isEmpty() ?: true
        )
        sut.getUserLogin()
        sut.uiState.test {
            val item1 = awaitItem()
            assertTrue(item1.uiData.firstName.isEmpty())

            val item2 = awaitItem()
            assertTrue(item2.uiData.firstName.isEmpty())
        }
    }

    @Test
    fun should_return_user_info_when_signed_in() = runTest {
        val expected = UserInfo(
            firstName = "a",
            lastName = "b",
            email = "abc@abc.com",
            phoneNumber = "+22222222222"
        )
        appPreferenceDataStore.updateDataStore(
            PreferenceKeys.userInfo,
            json.encodeToString(expected)
        )
        // check value is in datastore
        assertTrue(
            appPreferenceDataStore.getDataStore(PreferenceKeys.userInfo).first()?.isNotEmpty() ?: false
        )

        sut.getUserLogin()
        sut.uiState.test {
            val item1 = awaitItem()
            assertTrue(item1.uiData.firstName.isEmpty())

            val actual = awaitItem()
            assertTrue(actual.uiData.firstName.isNotEmpty())
            Assert.assertEquals(expected, actual.uiData)
        }
    }

    @Test
    fun should_return_no_user_on_logout() = runBlocking {

        val userInfo = UserInfo(
            firstName = "a",
            lastName = "b",
            email = "abc@abc.com",
            phoneNumber = "+22222222222"
        )
        appPreferenceDataStore.updateDataStore(
            PreferenceKeys.userInfo,
            json.encodeToString(userInfo)
        )

        // check value is in datastore
        assertTrue(
            appPreferenceDataStore.getDataStore(PreferenceKeys.userInfo).first()?.isNotEmpty() ?: false
        )

        sut.onLogout()
        delay(1000)
        // check value is not in datastore
        assertTrue(
            appPreferenceDataStore.getDataStore(PreferenceKeys.userInfo).first()?.isEmpty() ?: true
        )
    }

    @After
    fun shutDown() = runTest {
        appPreferenceDataStore.clear()
    }
}