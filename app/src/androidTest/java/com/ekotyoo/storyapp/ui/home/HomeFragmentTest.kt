package com.ekotyoo.storyapp.ui.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ekotyoo.storyapp.data.datasource.remote.api.ApiConfig
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    private val mockWebServer = MockWebServer()
    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
    }
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}