package br.com.bootcamp.magicgamecs.features.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.bootcamp.magicgamecs.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    fun testRecyclerViewClick() {
        onView(withId(R.id.recyclerView_main)).perform(RecyclerViewActions.actionOnItemAtPosition<CollectionAdapter.CollectionViewHolder>(0, click()))
    }
}