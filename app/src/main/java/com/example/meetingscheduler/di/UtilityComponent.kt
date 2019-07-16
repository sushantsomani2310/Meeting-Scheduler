package com.example.meetingscheduler.di

import com.example.meetingscheduler.data.MeetingAdapter
import com.example.meetingscheduler.view.AddMeetingActivity
import com.example.meetingscheduler.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface UtilityComponent {
    fun inject(mainActivity: MainActivity)

    fun injectMeetingAdapter(meetingAdapter: MeetingAdapter)

    fun injectAddMeetingActivity(addMeetingActivity: AddMeetingActivity)
}
