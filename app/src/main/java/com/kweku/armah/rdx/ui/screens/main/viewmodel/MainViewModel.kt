package com.kweku.armah.rdx.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kweku.armah.rdx.data.AppPreferenceDataStore
import com.kweku.armah.rdx.data.PreferenceKeys
import com.kweku.armah.rdx.domain.model.UserInfo
import com.kweku.armah.rdx.domain.wrapper.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appPreferenceDataStore: AppPreferenceDataStore,
    private val json: Json
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState(UserInfo()))
    val uiState = _uiState.asStateFlow()


    fun getUserLogin() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            appPreferenceDataStore.getDataStore(PreferenceKeys.userInfo).collectLatest { data ->
                data?.let {
                    _uiState.update {
                        if (data.isNotEmpty()) {
                            it.copy(
                                uiData = json.decodeFromString(data),
                                isLoading = false
                            )
                        } else {
                            it.copy(
                                uiData = UserInfo(),
                                isLoading = false
                            )
                        }
                    }
                } ?: run {
                    _uiState.update {
                        it.copy(
                            uiData = UserInfo(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            appPreferenceDataStore.clear()
        }
    }
}

