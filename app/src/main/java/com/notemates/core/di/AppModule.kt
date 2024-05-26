package com.notemates.core.di

import android.app.Application
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import com.notemates.data.sources.local.Preferences
import com.notemates.data.sources.remote.ApiConfig
import com.notemates.data.sources.remote.NoteApi
import com.notemates.data.sources.remote.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferences(
        application: Application,
    ): Preferences = Preferences(application)

    @Provides
    @Singleton
    fun providerUserApi(): UserApi = ApiConfig.retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideNoteApi(): NoteApi = ApiConfig.retrofit.create(NoteApi::class.java)

    @Provides
    @Singleton
    fun providerAuthRepository(
        userApi: UserApi,
        application: Application,
        preferences: Preferences,
    ): AuthRepository = AuthRepository(
        userApi, application, preferences
    )

    @Provides
    @Singleton
    fun provideNoteRepository(
        application: Application,
        noteApi: NoteApi,
    ): NoteRepository = NoteRepository(
        application = application,
        noteApi = noteApi,
    )
}