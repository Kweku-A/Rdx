package com.kweku.armah.rdx.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val userInfo: Preferences.Key<String> = stringPreferencesKey(name = "user")
}