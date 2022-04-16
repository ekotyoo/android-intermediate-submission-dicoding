package com.ekotyoo.storyapp.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekotyoo.storyapp.data.repositories.StoryRepository
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.di.Injection
import com.ekotyoo.storyapp.ui.home.HomeViewModel
import com.ekotyoo.storyapp.ui.login.LoginViewModel
import com.ekotyoo.storyapp.ui.maps.MapsViewModel
import com.ekotyoo.storyapp.ui.poststory.PostStoryViewModel
import com.ekotyoo.storyapp.ui.signup.SignupViewModel

class ViewModelFactory(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(PostStoryViewModel::class.java) -> {
                PostStoryViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(userRepository, storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository(context), Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}