package com.example.meetingscheduler.view

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.example.meetingscheduler.R
import com.example.meetingscheduler.Utility
import com.example.meetingscheduler.data.MeetingAdapter
import com.example.meetingscheduler.data.MeetingData
import com.example.meetingscheduler.di.DaggerUtilityComponent
import com.example.meetingscheduler.di.UtilityComponent
import com.example.meetingscheduler.model.Meeting
import com.example.meetingscheduler.viewmodel.MainViewModel
import java.util.*
import javax.inject.Inject
import kotlin.Comparator

class MainActivity : AppCompatActivity(), MeetingData.MeetingDataResponseListener {

    @Inject
    lateinit var utility: Utility

    companion object{
        var SELECTED_DATE_INTENT = "com.example.meetingscheduler.view.MainActivity.SELECTED_DATE_INTENT"
    }

    private lateinit var dateTextView: TextView

    private lateinit var mainViewModel: MainViewModel

    private lateinit var progressBar: ProgressBar

    private lateinit var meetingRecyclerView: RecyclerView

    private lateinit var meetingAdapter: MeetingAdapter

    private lateinit var todaysDate: String

    private lateinit var noMeetingImageView: ImageView

    private lateinit var noMeetingTextView: TextView

    private lateinit var scheduleMeetingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        //start Progress bar
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        //fetch current date
        val utilityComponent: UtilityComponent = DaggerUtilityComponent.create()
        utilityComponent.inject(this)
        todaysDate = utility.getTodaysDate() //"07-07-2015"
        //call API to fetch meetings of current date
        showMeetings(todaysDate)

        //set current date in date textview
        dateTextView = findViewById(R.id.date_textView)
        noMeetingImageView = findViewById(R.id.no_meeting_imageView)
        noMeetingTextView = findViewById(R.id.no_meetings_textView)
        scheduleMeetingButton = findViewById(R.id.schedle_button)
        dateTextView.text = todaysDate

        meetingRecyclerView = findViewById(R.id.meeting_recyclerview)
        meetingAdapter = MeetingAdapter()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        meetingRecyclerView.layoutManager = layoutManager
        meetingRecyclerView.adapter = meetingAdapter
        meetingRecyclerView.setHasFixedSize(true)
    }

    /**
     * callback methods triggered when retrofit fetches meetings of the date
     */
    override fun getMeetingResponse(meetings: List<Meeting>?) {
        //stop progress bar
        //set data to adapter of recycler view
        sortMeetingByStartTime(meetings)
        meetingAdapter.setMeetings(meetings)
        progressBar.visibility = View.GONE
        //if list is empty user can cheer up for the day
        if (meetings?.size == 0) {
            noMeetingImageView.visibility = View.VISIBLE
            noMeetingTextView.visibility = View.VISIBLE
        } else {
            noMeetingImageView.visibility = View.GONE
            noMeetingTextView.visibility = View.GONE
        }
    }

    /**
     * error callback method, triggred while getting any error in fetching meetings
     */
    override fun getErrorResponse(errorMessage: String?) {
        //stop progress bar and show error message
        progressBar.visibility = View.GONE
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        Log.d("ERROR", errorMessage)
    }

    /**
     * fetch the meetings of a given date
     */
    private fun showMeetings(todaysDate: String) {
        mainViewModel.fetchMeetings(todaysDate, this)
    }

    override fun onResume() {
        super.onResume()
        //handle schedule meeting button click
        scheduleMeetingButton.setOnClickListener {
            //start AddMeeting activity
            val addMeetingIntent = Intent(this, AddMeetingActivity::class.java)
            addMeetingIntent.putExtra(SELECTED_DATE_INTENT,todaysDate)
            startActivity(addMeetingIntent)
        }
    }

    /**
     * function gets the meeting of previous day
     */
    fun fetchPreviousDayMeeting(v:View){
        val newDate = utility.getPrevDate(todaysDate)
        mainViewModel.fetchMeetings(newDate, this)
        dateTextView.text = newDate
        todaysDate = newDate
    }

    /**
     * function gets the meeting of next day
     */
    fun fetchNextDayMeeting(v:View){
        val newDate = utility.getNextDate(todaysDate)
        mainViewModel.fetchMeetings(newDate, this)
        dateTextView.text = newDate
        todaysDate = newDate
    }

    fun sortMeetingByStartTime(meetings:List<Meeting>?){
        Collections.sort(meetings,MeetingSorter(utility))
    }

    class MeetingSorter : Comparator<Meeting>{
        var innerUtility:Utility
        constructor(innerUtility: Utility){
            this.innerUtility=innerUtility
        }
        override fun compare(o1: Meeting?, o2: Meeting?): Int {
            return innerUtility.compareTwoTime(o1?.startTime.toString(),o2?.startTime.toString())
        }

    }
}
