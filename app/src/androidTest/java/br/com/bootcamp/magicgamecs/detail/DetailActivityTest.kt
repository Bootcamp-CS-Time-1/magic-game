package br.com.bootcamp.magicgamecs.detail

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.features.CardDetailActivity
import br.com.bootcamp.magicgamecs.models.pojo.Card
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @get:Rule
    var activityTestRule =
        object : IntentsTestRule<CardDetailActivity>(CardDetailActivity::class.java) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                val card =
                    Card("35465", "", "Monster", "Monster", ArrayList(), "Primeira", "Primeira")
                val result = Intent(targetContext, CardDetailActivity::class.java)
                result.putExtra("CARD", card)
                return result
            }
        }

    @Test
    fun whenActivityIsOpen_shouldShowCardImage() {
        //find the view
        Espresso.onView(ViewMatchers.withId(R.id.imageView_card_item)).check(matches(isDisplayed()))
    }

    @Test
    fun whenClickAddFavoriteButton_shouldChangesTextButton() {
        //find the view
        Espresso.onView(ViewMatchers.withId(R.id.materialButton)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.materialButton))
            .check(matches(ViewMatchers.withText(R.string.remove_card_to_deck)))
    }

}