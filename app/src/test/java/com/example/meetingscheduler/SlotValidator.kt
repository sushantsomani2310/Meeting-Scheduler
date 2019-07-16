package com.example.meetingscheduler

import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.assertFalse
import org.junit.Test

class SlotValidator {

    /**
     * below test method checks for slot booking logic of Utility class
     */
    @Test
    fun isSlotAvailable(){
        val util:Utility = Utility()
        assertFalse(util.getTimeConflicts("13:00","14:30","13:00","15:00"))
        assertFalse(util.getTimeConflicts("12:30","13:30","13:00","15:00"))
        assertTrue(util.getTimeConflicts("12:30","13:00","13:00","15:00"))
        assertFalse(util.getTimeConflicts("13:00","13:30","13:00","15:00"))
        assertFalse(util.getTimeConflicts("13:00","18:00","13:00","15:00"))
        assertTrue(util.getTimeConflicts("16:00","17:00","13:00","15:00"))
        assertFalse(util.getTimeConflicts("14:30","15:30","13:00","15:00"))
    }
}