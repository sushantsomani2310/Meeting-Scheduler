package com.example.meetingscheduler

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.meetingscheduler.view.MainActivity
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var utility: Utility

    @Rule
    @JvmField
    var xyz = IntentsTestRule(MainActivity::class.java)

    @Before
    fun initializeInstance(){
        utility = Utility()
    }

    /**
     * test that current date is set in Main activity upon launching it
     * also tests that all buttons to navigate are visible
     */
    @Test
    fun checkViews(){
        val expectedDate = utility.getTodaysDate()
        onView(withId(R.id.date_textView)).check(matches(withText(expectedDate)))
        onView(withId(R.id.prev_textview)).check(matches(withText("PREV")))
        onView(withId(R.id.textView4)).check(matches(withText("NEXT")))
        onView(withId(R.id.schedle_button)).check(matches(withText("Schedule Company Meeting")))
    }

    /**
     * verifies that corresponding activity is opened upon pressing the button of schedule meeting
     */
    @Test
    fun checkDateIntent(){
        onView(withId(R.id.schedle_button)).perform(click())
        intended(allOf(
                hasComponent(hasShortClassName(".view.AddMeetingActivity")),
                toPackage("com.example.meetingscheduler")
        ))
    }
}