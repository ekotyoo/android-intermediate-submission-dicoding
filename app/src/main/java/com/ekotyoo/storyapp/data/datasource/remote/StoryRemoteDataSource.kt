package com.ekotyoo.storyapp.data.datasource.remote

import com.ekotyoo.storyapp.data.datasource.remote.api.StoryApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRemoteDataSource(private val storyApi: StoryApi) {

    suspend fun getStories(token: String, page: Int, size: Int) =
        storyApi.getStories("Bearer $token", page, size)

    suspend fun getStoriesWithLocation(token: String, page: Int, size: Int) =
        storyApi.getStories("Bearer $token", page, size, 1)

    suspend fun postStories(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ) = storyApi.postStory("Bearer $token", file, description, lat, lon)
}