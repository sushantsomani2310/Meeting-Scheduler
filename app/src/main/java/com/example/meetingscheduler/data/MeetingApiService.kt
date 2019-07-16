package com.example.meetingscheduler.data

import com.example.meetingscheduler.model.Meeting
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MeetingApiService {

    @GET("schedule")
    fun getMeetings(@Query("date") date:String):Call<List<Meeting>>
}
