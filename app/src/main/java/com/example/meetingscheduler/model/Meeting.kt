package com.example.meetingscheduler.model

import com.google.gson.annotations.SerializedName

data class Meeting (@SerializedName("start_time") var startTime:String,@SerializedName("end_time") var endTime:String,
                    @SerializedName("description") var description:String,@SerializedName("participants") var participants:List<String>)
