package com.ekotyoo.storyapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.ekotyoo.storyapp.data.StoryRemoteMediator
import com.ekotyoo.storyapp.data.datasource.local.StoryDatabase
import com.ekotyoo.storyapp.data.datasource.remote.StoryRemoteDataSource
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.utils.StoryError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val storyRemoteDataSource: StoryRemoteDataSource,
    private val storyDatabase: StoryDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String): LiveData<PagingData<StoryModel>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(token, storyRemoteDataSource, storyDatabase),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }).liveData
    }

    suspend fun getStoriesWithLocation(token: String): List<StoryModel> {
        val stories = mutableListOf<StoryModel>()
        withContext(Dispatchers.IO) {
            try {
                val response = storyRemoteDataSource.getStoriesWithLocation(token, 1, 20)
                if (response.isSuccessful) {
                    response.body()?.listStory?.forEach {
                        stories.add(
                            StoryModel(
                                id = it.id,
                                name = it.name,
                                createdAt = it.createdAt,
                                imageUrl = it.photoUrl,
                                caption = it.description,
                                lat = it.lat,
                                lon = it.lon
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
                throw StoryError(e.message.toString())
            }
        }

        return stories
    }

    suspend fun postStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latLng: LatLng?
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = if (latLng != null) {
                    storyRemoteDataSource.postStories(token, file, description, latLng.latitude, latLng.longitude)
                } else {
                    storyRemoteDataSource.postStories(token, file, description)
                }

                if (!response.isSuccessful) {
                    throw StoryError(response.message())
                }
            } catch (e: Throwable) {
                throw StoryError(e.message.toString())
            }
        }
    }
}