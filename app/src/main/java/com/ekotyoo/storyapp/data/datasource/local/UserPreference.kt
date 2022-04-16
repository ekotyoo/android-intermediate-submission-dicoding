package com.ekotyoo.storyapp.data.datasource.local


import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ekotyoo.storyapp.model.UserModel


class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                id = preferences[KEY_ID],
                email = preferences[KEY_EMAIL],
                name = preferences[KEY_NAME],
                token = preferences[KEY_TOKEN],
                password = preferences[KEY_PASSWORD],
                isLoggedIn = preferences[KEY_IS_LOGGED_IN]
            )
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[KEY_ID] = user.id ?: ""
            preferences[KEY_EMAIL] = user.email ?: ""
            preferences[KEY_NAME] = user.name ?: ""
            preferences[KEY_TOKEN] = user.token ?: ""
            preferences[KEY_PASSWORD] = user.password ?: ""
            preferences[KEY_IS_LOGGED_IN] = user.isLoggedIn ?: false
        }
    }

    suspend fun updateUser(user: UserModel) {
        dataStore.edit { preferences ->
            user.id?.let { preferences[KEY_ID] = it }
            user.email?.let { preferences[KEY_EMAIL] = it }
            user.name?.let { preferences[KEY_NAME] = it }
            user.token?.let { preferences[KEY_TOKEN] = it }
            user.password?.let { preferences[KEY_PASSWORD] = it }
            user.isLoggedIn?.let { preferences[KEY_IS_LOGGED_IN] = it }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val KEY_ID = stringPreferencesKey("id")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_PASSWORD = stringPreferencesKey("password")
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}