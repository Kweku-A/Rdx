package com.kweku.armah.rdx.di

import android.content.Context
import com.kweku.armah.rdx.data.AppPreferenceDataStore
import com.kweku.armah.rdx.data.AppPreferenceDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoreDataModule {

    @Singleton
    @Provides
    fun providePreferenceStore(
        @ApplicationContext context: Context,
        dispatcher: CoroutineDispatcher,
    ): AppPreferenceDataStore {
        return AppPreferenceDataStoreImpl(context, dispatcher)
    }

    @Singleton
    @Provides
    fun provideSerializer(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}