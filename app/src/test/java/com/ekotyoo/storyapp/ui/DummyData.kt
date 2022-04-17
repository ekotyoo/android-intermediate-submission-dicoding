package com.ekotyoo.storyapp.ui

import androidx.paging.PagingData
import com.ekotyoo.storyapp.data.datasource.remote.responses.StoryItem
import com.ekotyoo.storyapp.model.StoryModel

object DummyData {
    fun generateStoryItem(): List<StoryItem> {
        val listItem = mutableListOf<StoryItem>()
        repeat(10) {
            listItem.add(
                StoryItem(
                    photoUrl = "https://www.google-$it.com",
                    createdAt = "createdAt-$it",
                    name = "name-$it",
                    description = "desc-$it",
                    lon = it.toDouble(),
                    id = it.toString(),
                    lat = it.toDouble(),
                )
            )
        }
        return listItem
    }

    fun generatePagingDataStoryModel(): PagingData<StoryModel> = PagingData.from(getListStoryModel())

    fun getListStoryModel(): List<StoryModel> {
        val stories = mutableListOf<StoryModel>()
        generateStoryItem().forEach {
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

        return stories
    }
}