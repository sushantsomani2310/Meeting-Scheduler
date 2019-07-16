package com.example.meetingscheduler

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Utility @Inject constructor() {

    fun getTodaysDate():String{
        val date:Date = java.util.Calendar.getInstance().time
        val df:SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        return df.format(date)
    }

    fun getProperTimeFormat(rawTimeFormat:String):String?{
        var newTimeFormat : String?=null
        try {
            val sdf_24hrs = SimpleDateFormat("HH:mm")
            val sdf_12hrs = SimpleDateFormat("hh:mm a")
            val _24HourDt:Date = sdf_24hrs.parse(rawTimeFormat)
            newTimeFormat = sdf_12hrs.format(_24HourDt)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return newTimeFormat
    }

    fun getNextDate(currentDate:String):String{
        val oldDate = SimpleDateFormat("dd-MM-yyyy").parse(currentDate)
        val newDate = Date(oldDate.time+86400000)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(newDate)
    }

    fun getPrevDate(currentDate:String):String{
        val oldDate = SimpleDateFormat("dd-MM-yyyy").parse(currentDate)
        val newDate = Date(oldDate.time-86400000)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(newDate)
    }

    /**
     * check for conflicts while booking the meeting slots
     */
    fun getTimeConflicts(time1:String,time2:String,bookedStartTime:String,bookedEndTime:String):Boolean{
        val startTime:Date = SimpleDateFormat("HH:mm").parse(time1)
        val endTime:Date = SimpleDateFormat("HH:mm").parse(time2)
        val alreadyBookedStartTime:Date = SimpleDateFormat("HH:mm").parse(bookedStartTime)//1
        val alreadyBookedEndTime:Date = SimpleDateFormat("HH:mm").parse(bookedEndTime)//3

        if((startTime.compareTo(alreadyBookedStartTime)>=0)&&(startTime.compareTo(alreadyBookedEndTime)<0)) return false
        if((endTime.compareTo(alreadyBookedStartTime)>0)&&(endTime.compareTo(alreadyBookedEndTime)<=0)) return false
        return true
    }

    fun compareTwoTime(time1:String,time2:String):Int{
        val sdf = SimpleDateFormat("HH:mm")
        val t1 = sdf.parse(time1)
        val t2 = sdf.parse(time2)
        if(t1.after(t2)) return 1
        else if(t1.before(t2)) return -1
        return 0
    }
}
