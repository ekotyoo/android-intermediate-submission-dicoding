package com.ekotyoo.storyapp.ui.poststory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ekotyoo.storyapp.data.repositories.StoryRepository
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.model.UserModel
import com.ekotyoo.storyapp.ui.TestCoroutineRule
import com.ekotyoo.storyapp.ui.login.getOrAwaitValue
import com.ekotyoo.storyapp.utils.AuthError
import com.ekotyoo.storyapp.utils.StoryError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PostStoryViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val userRepository: UserRepository = mock(UserRepository::class.java)
    private val storyRepository: StoryRepository = mock(StoryRepository::class.java)

    private lateinit var postStoryViewModel: PostStoryViewModel

    @Before
    fun setUp() {
        `when`(userRepository.userModel).thenReturn(
            flow {
                emit(UserModel(email = "dummy@mail.com", password = "password"))
            }
        )

        postStoryViewModel = PostStoryViewModel(userRepository, storyRepository)
    }

    @Test
    fun `when userModel observed should return UserModel`() = testCoroutineRule.runBlockingTest {
        val actualUser = postStoryViewModel.userModel.getOrAwaitValue()
        Assert.assertNotNull(actualUser)
    }

    @Test
    fun `when uploadImage success should not throw error and isSuccess`() = testCoroutineRule.runBlockingTest {
        val token = "token"
        val file = MultipartBody.Part.createFormData("file", "dummyfile")
        val desc = "desc".toRequestBody("text/plain".toMediaType())
        val latLng = LatLng(0.0, 0.0)

        postStoryViewModel.uploadImage(token, file, desc, latLng)
        Mockito.verify(storyRepository).postStory(token, file, desc, latLng)
        Assert.assertTrue(postStoryViewModel.isSuccess.getOrAwaitValue())
    }

    @Test
    fun `when uploadImage success should throw error`() = testCoroutineRule.runBlockingTest {
        val token = "token"
        val file = MultipartBody.Part.createFormData("file", "dummyfile")
        val desc = "desc".toRequestBody("text/plain".toMediaType())
        val latLng = LatLng(0.0, 0.0)

        Mockito.doThrow(StoryError("Error")).`when`(storyRepository).postStory(token, file, desc, latLng)
        postStoryViewModel.uploadImage(token, file, desc, latLng)
        Mockito.verify(storyRepository).postStory(token, file, desc, latLng)
        Assert.assertTrue(postStoryViewModel.errorMessage.getOrAwaitValue() == "Error")
    }
}