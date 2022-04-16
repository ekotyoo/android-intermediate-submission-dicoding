package com.ekotyoo.storyapp.ui.home

import androidx.lifecycle.*
import com.ekotyoo.storyapp.data.repositories.StoryRepository
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.utils.AuthError
import com.ekotyoo.storyapp.utils.StoryError
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {

    val userModel = userRepository.userModel.asLiveData()

    private val _stories = MutableLiveData<List<StoryModel>>()
    val stories: LiveData<List<StoryModel>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getStories(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val stories = storyRepository.getStories(token)
                _stories.value = stories
            } catch (e: StoryError) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.logout()
            } catch (e: AuthError) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}