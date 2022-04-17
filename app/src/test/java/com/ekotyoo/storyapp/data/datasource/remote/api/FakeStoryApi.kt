package com.ekotyoo.storyapp.data.datasource.remote.api

import com.ekotyoo.storyapp.data.datasource.remote.responses.StoryPostResponse
import com.ekotyoo.storyapp.data.datasource.remote.responses.StoryResponse
import com.ekotyoo.storyapp.ui.DummyData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeStoryApi : StoryApi {

    var shouldThrowError = false

    override suspend fun getStories(
        token: String,
        page: Int,
        size: Int,
        location: Int
    ): Response<StoryResponse> {
        return if (shouldThrowError) {
            Response.error(401, "".toResponseBody("text/plain".toMediaType()))
        } else {
            Response.success(
                StoryResponse(
                    listStory = DummyData.generateStoryItem(),
                    error = false,
                    message = "Success"
                )
            )
        }
    }

    override suspend fun postStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): Response<StoryPostResponse> {
        return if (shouldThrowError) {
            Response.error(401, "".toResponseBody("text/plain".toMediaType()))
        } else {
            Response.success(StoryPostResponse(error = false, message = "Success"))
        }
    }
}