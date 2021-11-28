package com.example.yusakumaki.functionconfirm.fragments

import androidx.lifecycle.ViewModel
import com.example.yusakumaki.functionconfirm.extension.SingleLiveEvent

class ProfileViewModel: ViewModel() {

    val actionEvent = SingleLiveEvent<Unit>()

    fun onAction() {
        actionEvent.postValue(Unit)
    }
}