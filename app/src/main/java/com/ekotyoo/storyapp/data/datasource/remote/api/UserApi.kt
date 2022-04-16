package com.ekotyoo.storyapp.data.datasource.remote.api

import com.ekotyoo.storyapp.data.datasource.remote.responses.LoginResponse
import com.ekotyoo.storyapp.data.datasource.remote.responses.SignupResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignupResponse>
}