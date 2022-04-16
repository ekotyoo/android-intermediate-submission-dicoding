package com.ekotyoo.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ekotyoo.storyapp.data.datasource.remote.UserRemoteDataSource
import com.ekotyoo.storyapp.data.datasource.remote.apis.ApiConfig
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.data.datasource.local.UserPreference
import com.ekotyoo.storyapp.data.datasource.remote.StoryRemoteDataSource
import com.ekotyoo.storyapp.data.repositories.StoryRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userApi = ApiConfig.getUserApi()
        val userRemoteDataSource = UserRemoteDataSource(userApi)
        return UserRepository(context, userRemoteDataSource, UserPreference.getInstance(context.dataStore))
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val storyApi = ApiConfig.getStoryApi()
        val storyRemoteDataSource = StoryRemoteDataSource(storyApi)
        return StoryRepository(context, storyRemoteDataSource)
    }
}