package com.ekotyoo.storyapp.data.datasource.remote.apis

import com.ekotyoo.storyapp.data.datasource.remote.responses.StoryPostResponse
import com.ekotyoo.storyapp.data.datasource.remote.responses.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryApi {

    @GET("stories")
    suspend fun getStories(@Header("Authorization") token: String): Response<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Response<StoryPostResponse>
}