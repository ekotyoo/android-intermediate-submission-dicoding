package com.ekotyoo.storyapp.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.ekotyoo.storyapp.data.datasource.local.StoryDatabase
import com.ekotyoo.storyapp.data.datasource.remote.StoryRemoteDataSource
import com.ekotyoo.storyapp.data.datasource.remote.api.FakeStoryApi
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.ui.DummyData
import com.ekotyoo.storyapp.TestCoroutineRule
import com.ekotyoo.storyapp.ui.adapters.StoryAdapter
import com.ekotyoo.storyapp.ui.home.noopListUpdateCallback
import com.ekotyoo.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
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
    fun `when getStories should not return empty PagingData`() = testCoroutineRule.runBlockingTest {
        storyRepository = mock(StoryRepository::class.java)

        val token = "token"
        val expectedStories = DummyData.getListStoryModel()
        val expectedPagingData = DummyData.generatePagingDataStoryModel()
        val livedata = MutableLiveData<PagingData<StoryModel>>().apply {
            value = expectedPagingData
        }

        `when`(storyRepository.getStories(token)).thenReturn(livedata)

        val actualPagingData = storyRepository.getStories(token)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = testCoroutineRule.dispatcher,
            workerDispatcher = testCoroutineRule.dispatcher
        )

        differ.submitData(actualPagingData.getOrAwaitValue())
        advanceUntilIdle()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(expectedStories.size, differ.snapshot().size)
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
        storyRemoteDataSource =
            StoryRemoteDataSource(FakeStoryApi().apply { shouldThrowError = true })
        storyRepository = StoryRepository(storyRemoteDataSource, storyDatabase)
        val token = "token"

        val actualStories = storyRepository.getStoriesWithLocation(token)
        print(actualStories)

        Assert.assertTrue(actualStories.isNullOrEmpty())
    }
}