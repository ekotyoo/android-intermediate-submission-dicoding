package com.ekotyoo.storyapp.data.repositories

import android.content.Context
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.data.datasource.remote.StoryRemoteDataSource
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.utils.StoryError
import com.ekotyoo.storyapp.utils.withDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val context: Context, private val storyRemoteDataSource: StoryRemoteDataSource) {

    suspend fun getStories(token: String): List<StoryModel> {
        val listStory = mutableListOf<StoryModel>()
        withContext(Dispatchers.IO) {
            try {
                val response = storyRemoteDataSource.getStories(token)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()
                    body?.listStory?.forEach {
                        val storyModel = StoryModel(
                            id = it.id,
                            name = it.name,
                            createdAt = it.createdAt?.withDateFormat(),
                            imageUrl = it.photoUrl,
                            caption = it.description
                        )
                        listStory.add(storyModel)
                    }
                } else {
                    throw StoryError(context.getString(R.string.load_stories_failed))
                }
            } catch (e: Throwable) {
                throw StoryError(e.message.toString())
            }
        }
        return listStory
    }

    suspend fun postStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        withContext(Dispatchers.IO) {
            try {
                val response = storyRemoteDataSource.postStories(token, file, description)
                if (!response.isSuccessful) {
                    throw StoryError(response.message())
                }
            } catch (e: Throwable) {
                throw  StoryError(e.message.toString())
            }
        }
    }
}