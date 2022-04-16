package com.ekotyoo.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryModel(
    val id: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
    val imageUrl: String? = null,
    val caption: String? = null
) : Parcelable