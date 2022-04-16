package com.ekotyoo.storyapp.data.datasource.remote.api

import com.ekotyoo.storyapp.data.datasource.remote.responses.LoginResponse
import com.ekotyoo.storyapp.data.datasource.remote.responses.LoginResult
import com.ekotyoo.storyapp.data.datasource.remote.responses.SignupResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response

class FakeUserApi : UserApi {

    var shouldThrowError = false

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return if (shouldThrowError) {
            Response.error(401, ResponseBody.create("text/plain".toMediaType(), content = ""))
        } else {
            Response.success(
                LoginResponse(
                    loginResult = LoginResult("name", "id", "token"),
                    error = false,
                    message = "Success"
                )
            )
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Response<SignupResponse> {
        return if (shouldThrowError) {
            Response.error(401, ResponseBody.create("text/plain".toMediaType(), content = ""))
        } else {
            Response.success(
                SignupResponse(error = false, message = "Success")
            )
        }
    }
}