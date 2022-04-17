package com.ekotyoo.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.ekotyoo.storyapp.model.StoryModel
import com.ekotyoo.storyapp.ui.DummyData
import com.ekotyoo.storyapp.TestCoroutineRule
import com.ekotyoo.storyapp.ui.adapters.StoryAdapter
import com.ekotyoo.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `when stories should not empty`() = testCoroutineRule.runBlockingTest {
        val expectedStories = DummyData.getListStoryModel()
        val data = PagedTestDataSources.snapshot(expectedStories)
        val liveData = MutableLiveData<PagingData<StoryModel>>().apply { value = data }

        `when`(homeViewModel.stories).thenReturn(liveData)
        val actualNews = homeViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = testCoroutineRule.dispatcher,
            workerDispatcher = testCoroutineRule.dispatcher
        )

        differ.submitData(actualNews)
        advanceUntilIdle()

        Mockito.verify(homeViewModel).stories
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(expectedStories.size, differ.snapshot().size)
    }

}

class PagedTestDataSources private constructor() :
    PagingSource<Int, LiveData<List<StoryModel>>>() {
    companion object {
        fun snapshot(items: List<StoryModel>): PagingData<StoryModel> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}