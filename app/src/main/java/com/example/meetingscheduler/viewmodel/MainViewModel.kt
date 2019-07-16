package com.example.meetingscheduler.viewmodel

import android.arch.lifecycle.ViewModel
import com.example.meetingscheduler.MeetingRepository
import com.example.meetingscheduler.data.MeetingData
import com.example.meetingscheduler.di.DaggerRepositoryComponent
import com.example.meetingscheduler.di.RepositoryComponent
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var repository: MeetingRepository

    init {
        var repositoryComponent:RepositoryComponent = DaggerRepositoryComponent.create();
        repositoryComponent.injectMainViewModel(this)
    }

    /**
     * call method to fetch meetings
     */
    fun fetchMeetings(queryDate:String,meetingResponseListener: MeetingData.MeetingDataResponseListener){
        repository.fetchMeetings(queryDate,meetingResponseListener)
    }
}
