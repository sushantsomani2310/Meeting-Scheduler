package com.example.meetingscheduler.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.meetingscheduler.R
import android.app.DatePickerDialog
import java.util.*
import kotlin.properties.Delegates
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.meetingscheduler.Utility
import com.example.meetingscheduler.data.MeetingData
import com.example.meetingscheduler.di.DaggerUtilityComponent
import com.example.meetingscheduler.di.UtilityComponent
import com.example.meetingscheduler.model.Meeting
import com.example.meetingscheduler.viewmodel.AddMeetingViewModel
import javax.inject.Inject


class AddMeetingActivity : AppCompatActivity(),View.OnClickListener, MeetingData.MeetingDataResponseListener {

    @Inject
    lateinit var utility: Utility

    private lateinit var actualMeetingDate:String

    private lateinit var actualMeetingStartTime:String

    private lateinit var actualMeetingEndTime:String

    private lateinit var meetingDescription:String

    private lateinit var meetingDate:String

    private lateinit var meetingDateEditText:TextView

    private lateinit var meetingStartTimeEditText:TextView

    private lateinit var meetingEndTimeEditText:TextView

    private lateinit var meetingDescriptionEditText:EditText

    private lateinit var submitMeetingButton:Button

    private lateinit var addMeetingViewModel: AddMeetingViewModel

    private var mYear:Int by Delegates.notNull<Int>()
    private var mMonth:Int by Delegates.notNull<Int>()
    private var mDay:Int by Delegates.notNull<Int>()
    private var mHour:Int by Delegates.notNull<Int>()
    private var mMinute:Int by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meeting)

        meetingDate = intent.getStringExtra(MainActivity.SELECTED_DATE_INTENT)
        addMeetingViewModel = ViewModelProviders.of(this).get(AddMeetingViewModel::class.java)
        //initialize views here
        meetingDateEditText = findViewById(R.id.meeting_date_edittext)
        meetingStartTimeEditText = findViewById(R.id.meeting_start_time_edittext)
        meetingEndTimeEditText = findViewById(R.id.meeting_end_time_edittext)
        meetingDescriptionEditText = findViewById(R.id.description_editText)
        submitMeetingButton = findViewById(R.id.submit_button)
        meetingDateEditText.text = meetingDate

        meetingDateEditText.setOnClickListener(this)
        meetingStartTimeEditText.setOnClickListener(this)
        meetingEndTimeEditText.setOnClickListener(this)
        //fetch current date
        val utilityComponent: UtilityComponent = DaggerUtilityComponent.create()
        utilityComponent.injectAddMeetingActivity(this)

        var currentDate = utility.getTodaysDate()
        if(currentDate.compareTo(meetingDate)>=0) submitMeetingButton.isEnabled = false
        else  submitMeetingButton.isEnabled = true
    }

    override fun onClick(view: View?) {
        //handle date selector button click here
        if(view == meetingDateEditText){
            // Get Current Date
            val c:Calendar = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> meetingDateEditText.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
            //ensures no past date is selected
            datePickerDialog.datePicker.minDate = (System.currentTimeMillis()-1000)
        }

        else if(view == meetingStartTimeEditText){
            // Get Current Time
            val c:Calendar = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener {
                    view, hourOfDay, minute ->
                    //meetingStartTimeEditText.setText("$hourOfDay:$minute")
                    meetingStartTimeEditText.setText(String.format("%02d:%02d", hourOfDay, minute))
                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }

        else if(view == meetingEndTimeEditText){
            // Get Current Time
            val c:Calendar = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> meetingEndTimeEditText.setText(String.format("%02d:%02d", hourOfDay, minute)) },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        submitMeetingButton.setOnClickListener {
            //handle submit button click here
            if((meetingDateEditText.text.length!=0) ||(meetingStartTimeEditText.text.length!=0)
                ||(meetingEndTimeEditText.text.length!=0)||(meetingDescriptionEditText.text.length!=0)){
                actualMeetingDate = meetingDateEditText.text.toString()
                meetingDate = actualMeetingDate
                actualMeetingStartTime = meetingStartTimeEditText.text.toString()
                actualMeetingEndTime = meetingEndTimeEditText.text.toString()
                meetingDescription = meetingDescriptionEditText.text.toString().trim()
                //check time span & slot availability
                if(checkTimeSpan()) fetchMeetingSchedule(actualMeetingDate)
                else Toast.makeText(this,"End time should be after start time",Toast.LENGTH_LONG).show()
            }
            else{
                //show error about empty fields
                Toast.makeText(this,"Fill empty fields",Toast.LENGTH_LONG).show()
            }
        }

        meetingDateEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val currentDate = utility.getTodaysDate()
                if(currentDate.compareTo(s.toString())>=0) submitMeetingButton.isEnabled = false
                else  submitMeetingButton.isEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    /**
     * fetch meeting schedule from the API
     */
    private fun fetchMeetingSchedule(actualMeetingDate:String){
        addMeetingViewModel.fetchMeetings(actualMeetingDate,this)
    }

    /**
     * checks that end time is after start time
     */
    private fun checkTimeSpan():Boolean{
        if(actualMeetingStartTime.compareTo(actualMeetingEndTime)<0) return true
        return false
    }

    /**
     * check slot availability here
     */
    override fun getMeetingResponse(meetings: List<Meeting>?) {
        var isSlotAvailable = true
        for(m in meetings!!){
            if(!utility.getTimeConflicts(actualMeetingStartTime,actualMeetingEndTime,m.startTime,m.endTime)){
                isSlotAvailable = false
                break
            }
        }

        if(!isSlotAvailable) Toast.makeText(this,"Slot not available",Toast.LENGTH_LONG).show()
        else Toast.makeText(this,"Slot available",Toast.LENGTH_LONG).show()
    }

    /**
     * show error message to user here
     */
    override fun getErrorResponse(errorMessage: String?) {
        Toast.makeText(this,"Something went wrong!!",Toast.LENGTH_LONG).show()
    }

    /**
     * called when user want to go back to previous activity
     */
    fun finishCurrentActivity(v:View){
        finish()
    }
}
