package com.ekotyoo.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ekotyoo.storyapp.data.repositories.StoryRepository
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.model.UserModel
import com.ekotyoo.storyapp.ui.DummyData
import com.ekotyoo.storyapp.ui.TestCoroutineRule
import com.ekotyoo.storyapp.ui.login.getOrAwaitValue
import com.ekotyoo.storyapp.utils.StoryError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doThrow

@OptIn(ExperimentalCoroutinesApi::class)
class MapsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val userRepository: UserRepository = Mockito.mock(UserRepository::class.java)
    private val storyRepository: StoryRepository = Mockito.mock(StoryRepository::class.java)

    private lateinit var mapsViewModel: MapsViewModel

    @Before
    fun setUp() {
        `when`(userRepository.userModel).thenReturn(
            flow {
                emit(UserModel(email = "dummy@mail.com", password = "password"))
            }
        )
        mapsViewModel = MapsViewModel(userRepository, storyRepository)
    }

    @Test
    fun `when userModel observed should return UserModel`() = testCoroutineRule.runBlockingTest {
        val actualUser = mapsViewModel.userModel.getOrAwaitValue()
        Assert.assertNotNull(actualUser)
    }

    @Test
    fun `when getStoriesWithLocation success should not throw error`() = testCoroutineRule.runBlockingTest {
        val token = "token"
        val actualStories = DummyData.getListStoryModel()

        `when`(storyRepository.getStoriesWithLocation(token)).thenReturn(actualStories)
        mapsViewModel.getStoriesWithLocation(token)

        Mockito.verify(storyRepository).getStoriesWithLocation(token)

        val storyResults = mapsViewModel.stories.getOrAwaitValue()

        Assert.assertNotNull(storyResults)
        Assert.assertTrue(storyResults[0].name == "name-0")
    }

    @Test
    fun `when getStoriesWithLocation failed should throw error`() = testCoroutineRule.runBlockingTest {
        val token = "token"

        doThrow(StoryError("Error")).`when`(storyRepository).getStoriesWithLocation(token)
        mapsViewModel.getStoriesWithLocation(token)

        Mockito.verify(storyRepository).getStoriesWithLocation(token)
        Assert.assertTrue(mapsViewModel.errorMessage.getOrAwaitValue() == "Error")
    }
}