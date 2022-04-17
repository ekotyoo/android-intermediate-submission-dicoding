package com.ekotyoo.storyapp.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ekotyoo.storyapp.data.repositories.StoryRepository
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.utils.AuthError
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    val userModel = userRepository.userModel.asLiveData()

    val stories: LiveData<PagingData<StoryModel>> =
        userModel.switchMap {
            storyRepository.getStories(
                it.token ?: ""
            ).cachedIn(viewModelScope)
        }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

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