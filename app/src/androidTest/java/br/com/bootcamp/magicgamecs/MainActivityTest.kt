package br.com.bootcamp.magicgamecs

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import br.com.bootcamp.magicgamecs.features.home.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = IntentsTestRule(MainActivity::class.java)

    @Test
    fun givenInitialState_when_shold() {
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }
}