package com.example.yusakumaki.functionconfirm.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

class ProfileViewModel: ViewModel() {

    private val clickedState = LiveEvent<Unit>()
    val state: LiveData<Unit> = clickedState

    fun onAction() {
        clickedState.value = Unit
    }
}