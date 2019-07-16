package com.example.meetingscheduler.di

import com.example.meetingscheduler.viewmodel.AddMeetingViewModel
import com.example.meetingscheduler.viewmodel.MainViewModel
import dagger.Component

@Component
interface RepositoryComponent {

    fun injectMainViewModel(mainViewModel:MainViewModel)

    fun injectAddMeetingViewModel(addMeetingViewModel: AddMeetingViewModel)
}

