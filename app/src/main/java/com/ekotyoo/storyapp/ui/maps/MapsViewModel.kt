package com.ekotyoo.storyapp.ui.maps

import androidx.lifecycle.*
import com.ekotyoo.storyapp.data.repositories.StoryRepository
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.utils.StoryError
import kotlinx.coroutines.launch

class MapsViewModel(userRepository: UserRepository, private val storyRepository: StoryRepository) :
    ViewModel() {

    val userModel = userRepository.userModel.asLiveData()

    private val _stories = MutableLiveData<List<StoryModel>>()
    val stories: LiveData<List<StoryModel>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getStoriesWithLocation(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val stories = storyRepository.getStoriesWithLocation(token = token)
                _stories.value = stories
            } catch (e: StoryError) {
                _errorMessage.value = e.message.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
}