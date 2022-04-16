package com.ekotyoo.storyapp.data.repositories

import android.content.Context
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.data.datasource.remote.UserRemoteDataSource
import com.ekotyoo.storyapp.model.UserModel
import com.ekotyoo.storyapp.data.datasource.local.UserPreference
import com.ekotyoo.storyapp.utils.AuthError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val context: Context,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val prefs: UserPreference
) {

    val userModel = prefs.getUser()

    suspend fun login(email: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = userRemoteDataSource.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()?.loginResult
                    result?.let {
                        val user = UserModel(
                            id = it.userId,
                            email = email,
                            name = it.name,
                            password = password,
                            token = it.token,
                            isLoggedIn = true
                        )
                        prefs.updateUser(user)
                    }
                } else {
                    throw AuthError(context.getString(R.string.login_failed))
                }
            } catch (e: Throwable) {
                throw AuthError(e.message.toString())
            }
        }
    }

    suspend fun signup(name: String, email: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = userRemoteDataSource.signup(name, email, password)
                if (!response.isSuccessful) {
                    throw AuthError(response.body()?.message ?: context.getString(R.string.signup_failed))
                }
            } catch (e: Throwable) {
                throw AuthError(e.message.toString())
            }
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                prefs.updateUser(UserModel(token = null, isLoggedIn = false))
            } catch (e: Throwable) {
                throw AuthError(e.message.toString())
            }
        }
    }
}