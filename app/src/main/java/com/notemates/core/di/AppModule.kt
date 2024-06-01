package com.notemates.core.di

import android.app.Application
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import com.notemates.data.repositories.UserRepository
import com.notemates.data.sources.local.Preferences
import com.notemates.data.sources.remote.ApiConfig
import com.notemates.data.sources.remote.AuthApi
import com.notemates.data.sources.remote.NoteApi
import com.notemates.data.sources.remote.SearchApi
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
    fun provideAuthApi(): AuthApi = ApiConfig.retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideNoteApi(): NoteApi = ApiConfig.retrofit.create(NoteApi::class.java)

    @Provides
    @Singleton
    fun provideSearchApi(): SearchApi = ApiConfig.retrofit.create(SearchApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(): UserApi = ApiConfig.retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(
        application: Application,
        authApi: AuthApi,
        preferences: Preferences,
    ): AuthRepository = AuthRepository(
        application = application,
        authApi = authApi,
        preferences = preferences,
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

    @Provides
    @Singleton
    fun provideUserRepository(
        application: Application,
        userApi: UserApi,
    ) = UserRepository(
        application = application,
        userApi = userApi,
    )
}