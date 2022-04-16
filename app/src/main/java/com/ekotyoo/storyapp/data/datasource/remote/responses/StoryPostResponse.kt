package com.ekotyoo.storyapp.data.datasource.remote.responses

import com.google.gson.annotations.SerializedName

data class StoryPostResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
