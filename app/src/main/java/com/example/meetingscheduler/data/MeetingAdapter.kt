package com.example.meetingscheduler.data

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.meetingscheduler.R
import com.example.meetingscheduler.Utility
import com.example.meetingscheduler.di.DaggerUtilityComponent
import com.example.meetingscheduler.di.UtilityComponent
import com.example.meetingscheduler.model.Meeting
import javax.inject.Inject

class MeetingAdapter : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    private var meetings: List<Meeting>? = null

    @Inject
    lateinit var utility: Utility

    init {
        val utilityComponent: UtilityComponent = DaggerUtilityComponent.create()
        utilityComponent.injectMeetingAdapter(this)

    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MeetingViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        val view: View = layoutInflater.inflate(R.layout.meeting_recycler_item, viewGroup, false)
        return MeetingViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (meetings != null) return meetings!!.size
        return 0
    }

    override fun onBindViewHolder(meetingViewHolder: MeetingViewHolder, position: Int) {
        //set start time
        var time:String = meetings!!.get(position).startTime
        var properTimeFormat:String? = utility.getProperTimeFormat(time)
        meetingViewHolder.startTimeTextView.text = properTimeFormat + " -"

        //set end time
        time = meetings!!.get(position).endTime
        properTimeFormat = utility.getProperTimeFormat(time)
        meetingViewHolder.endTimeTextView.text = properTimeFormat

        if(meetings!!.get(position).description.length>50){
            val tempDesc = (meetings!!.get(position).description).substring(0, 50)
            meetingViewHolder.descriptionTextView.text = tempDesc+"..."
        }
        else meetingViewHolder.descriptionTextView.text = (meetings!!.get(position).description)
    }

    class MeetingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var startTimeTextView: TextView
        var endTimeTextView: TextView
        var descriptionTextView: TextView

        init {
            startTimeTextView = view.findViewById(R.id.start_time_textView)
            endTimeTextView = view.findViewById(R.id.end_time_textView)
            descriptionTextView = view.findViewById(R.id.description_textView)
        }
    }

    /**
     * sets the meetins in the Recycler view
     */
    fun setMeetings(meetings: List<Meeting>?) {
        this.meetings = meetings
        notifyDataSetChanged()
    }
}
