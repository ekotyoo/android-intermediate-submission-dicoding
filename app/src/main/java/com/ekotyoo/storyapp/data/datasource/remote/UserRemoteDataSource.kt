package com.ekotyoo.storyapp.data.datasource.remote

import com.ekotyoo.storyapp.data.datasource.remote.api.UserApi

class UserRemoteDataSource(private val userApi: UserApi) {

    suspend fun login(email: String, password: String) = userApi.login(email, password)

    suspend fun signup(name: String, email: String, password: String) =
        userApi.signup(name, email, password)
}