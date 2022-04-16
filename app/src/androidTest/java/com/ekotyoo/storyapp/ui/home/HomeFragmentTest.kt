package com.ekotyoo.storyapp.ui.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.data.datasource.remote.api.ApiConfig
import com.ekotyoo.storyapp.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("story_db")
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getStories_success() {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_StoryApp)

        val stringBody = JsonConverter.readStringFromFile("story_success_response.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(stringBody)

        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
    }

    @Test
    fun getStories_failed() {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_StoryApp)

        val stringBody = JsonConverter.readStringFromFile("story_error_response.json")
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(stringBody)

        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.tv_empty)).check(matches(isDisplayed()))
    }
}