package com.example.meetingscheduler

import com.example.meetingscheduler.data.MeetingData
import javax.inject.Inject

class MeetingRepository @Inject constructor(){

    /**
     * call method to fetch meetings
     */
    fun fetchMeetings(queryDate:String,meetingResponseListener: MeetingData.MeetingDataResponseListener){
        MeetingData.fetchMeetings(queryDate,meetingResponseListener)
    }
}
