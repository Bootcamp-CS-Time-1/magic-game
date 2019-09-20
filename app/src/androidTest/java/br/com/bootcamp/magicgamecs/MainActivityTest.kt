package br.com.bootcamp.magicgamecs

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.bootcamp.magicgamecs.features.home.MainActivity
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = IntentsTestRule(MainActivity::class.java)


}