package com.example.meetingscheduler.data

import com.example.meetingscheduler.model.Meeting
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MeetingData {

    const val MEETING_API_BASE_URL = "http://fathomless-shelf-5846.herokuapp.com/api/"
    private lateinit var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(MEETING_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    public interface MeetingDataResponseListener {
        fun getMeetingResponse(meetings: List<Meeting>?)
        fun getErrorResponse(errorMessage:String?)
    }

    /**
     * call method to fetch meetings
     */
    fun fetchMeetings(queryDate: String,responseListener: MeetingDataResponseListener) {
        var meetingApiService: MeetingApiService = retrofit.create(MeetingApiService::class.java)
        var call: Call<List<Meeting>> = meetingApiService.getMeetings(queryDate)
        call.enqueue(object : Callback<List<Meeting>> {
            override fun onFailure(call: Call<List<Meeting>>, t: Throwable) {
                responseListener.getErrorResponse(t.message)
            }

            override fun onResponse(call: Call<List<Meeting>>, response: Response<List<Meeting>>) {
                responseListener.getMeetingResponse(response.body())
            }
        })
    }
}
