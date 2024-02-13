package com.kweku.armah.rdx.data

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface AppPreferenceDataStore {
    suspend fun<T> updateDataStore(key: Preferences.Key<T>, value: T): Preferences
    fun<T> getDataStore(key: Preferences.Key<T>): Flow<T?>
    suspend fun clear()
}