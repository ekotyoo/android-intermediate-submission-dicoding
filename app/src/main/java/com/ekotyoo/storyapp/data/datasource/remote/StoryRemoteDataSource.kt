package com.ekotyoo.storyapp.data.datasource.remote

import com.ekotyoo.storyapp.data.datasource.remote.apis.StoryApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRemoteDataSource(private val storyApi: StoryApi) {

    suspend fun getStories(token: String) = storyApi.getStories("Bearer $token")

    suspend fun postStories(token: String, file: MultipartBody.Part, description: RequestBody) = storyApi.postStory("Bearer $token", file, description)
}