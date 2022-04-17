package com.ekotyoo.storyapp.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ekotyoo.storyapp.R
import com.ekotyoo.storyapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun load_story() {
        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                2
            )
        )
    }

    @Test
    fun go_to_detail_story() {
        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                click()
            )
        )
    }

    @Test
    fun go_to_map_story() {
        onView(withId(R.id.btn_map)).perform(click())
        pressBack()
    }

    @Test
    fun go_to_post_story() {
        onView(withId(R.id.btn_add_story)).perform(click())
        onView(withId(R.id.btn_change_image)).perform(click())
        pressBack()
        onView(withId(R.id.et_caption)).perform(typeText("Hello World"))
        pressBack()
    }

    @Test
    fun open_popup_menu() {
        onView(withId(R.id.btn_menu)).perform(click())
    }
}