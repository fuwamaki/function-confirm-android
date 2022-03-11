package com.example.yusakumaki.functionconfirm.fragments

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hadilq.liveevent.LiveEvent

val AndroidViewModel.context: Context
    get() = getApplication()

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val clickedState = LiveEvent<Unit>()
    val state: LiveData<Unit> = clickedState

    fun onAction() {
        clickedState.value = Unit
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
    }
}