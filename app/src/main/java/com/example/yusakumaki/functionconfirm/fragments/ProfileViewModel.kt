package com.example.yusakumaki.functionconfirm.fragments

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.helper.StepCountHelper
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber

val AndroidViewModel.context: Context
    get() = getApplication()

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val clickedState = LiveEvent<Unit>()
    val state: LiveData<Unit> = clickedState

    private val checkGoogleSignInState = LiveEvent<Unit>()
    val checkGoogleSignIn: LiveData<Unit> = checkGoogleSignInState

    private val _openSettingEvent = LiveEvent<Unit>()
    val openSettingEvent: LiveData<Unit> = _openSettingEvent

    private val _stepCountText = MutableLiveData("0")
    val stepCountText: LiveData<String> = _stepCountText

    private val _permissionState = MutableLiveData("")
    val permissionState: LiveData<String> = _permissionState

    private val _googleFitAppState = MutableLiveData("")
    val googleFitAppState: LiveData<String> = _googleFitAppState

    fun updatePermission() {
        _permissionState.postValue(
            context.getString(
                if (StepCountHelper.hasFitnessPermission(
                        context
                    )
                ) R.string.text_allow else R.string.text_not_allow
            )
        )
        _googleFitAppState.postValue(
            context.getString(
                if (StepCountHelper.hasGoogleFitApplication(
                        context
                    )
                ) R.string.text_have_google_fit else R.string.text_not_have_google_fit
            )
        )
    }

    fun onAction() {
        clickedState.value = Unit
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
    }

    fun requestStepCountPermission() {
        checkGoogleSignInState.postValue(Unit)
    }

    fun segueToSetting() {
        _openSettingEvent.postValue(Unit)
    }

    fun updateStepCount() {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        flow {
            emit(StepCountHelper.readTodayStepOriginalCount(context))
        }.catch {
            Timber.e(it)
        }.collectLatest {
            Timber.v("collectLatest: $it")
            _stepCountText.postValue(it.toString())
        }
    }
}