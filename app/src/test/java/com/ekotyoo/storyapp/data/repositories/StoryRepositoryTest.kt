package com.ekotyoo.storyapp.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ekotyoo.storyapp.data.datasource.local.StoryDatabase
import com.ekotyoo.storyapp.data.datasource.remote.StoryRemoteDataSource
import com.ekotyoo.storyapp.data.datasource.remote.api.FakeStoryApi
import com.ekotyoo.storyapp.data.datasource.remote.api.StoryApi
import com.ekotyoo.storyapp.ui.DummyData
import com.ekotyoo.storyapp.ui.TestCoroutineRule
import com.ekotyoo.storyapp.ui.login.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyRemoteDataSource: StoryRemoteDataSource
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp() {
        storyRemoteDataSource = StoryRemoteDataSource(FakeStoryApi())
        storyRepository = StoryRepository(storyRemoteDataSource, storyDatabase)
    }

    @Test
    fun `when getStoriesWithLocation should not return empty list`() = runBlocking {
        val token = "token"
        val expectedStories = DummyData.generateStoryItem()
        val actualStories = storyRepository.getStoriesWithLocation(token)

        Assert.assertTrue(actualStories.isNotEmpty())
        Assert.assertEquals(expectedStories[0].id, actualStories[0].id)
    }

    @Test
    fun `when getStoriesWithLocation should return empty list`() = runBlocking {
        storyRemoteDataSource = StoryRemoteDataSource(FakeStoryApi().apply { shouldThrowError = true })
        storyRepository = StoryRepository(storyRemoteDataSource, storyDatabase)
        val token = "token"

        val actualStories = storyRepository.getStoriesWithLocation(token)
        print(actualStories)

        Assert.assertTrue(actualStories.isNullOrEmpty())
    }
}