package com.example.yusakumaki.functionconfirm.fragments

import android.app.Application
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yusakumaki.functionconfirm.R
import com.example.yusakumaki.functionconfirm.helper.StepCountHelper
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.net.Inet4Address

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

    private val _advertisingId = MutableLiveData("")
    val advertisingId: LiveData<String> = _advertisingId

    private val _globalIPAddress = MutableLiveData("")
    val globalIPAddress: LiveData<String> = _globalIPAddress

    private val _localIPAddress = MutableLiveData("")
    val localIPAddress: LiveData<String> = _localIPAddress

    private val _ssid = MutableLiveData("")
    val ssid: LiveData<String> = _ssid

    private val _bssid = MutableLiveData("")
    val bssid: LiveData<String> = _bssid

    private val okHttpClient = OkHttpClient.Builder().build()

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

    @OptIn(DelicateCoroutinesApi::class)
    fun requestAdvertisingId() {
        GlobalScope.launch {
            try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(context)
                if (!info.isLimitAdTrackingEnabled) {
                    _advertisingId.postValue(info.id)
                } else {
                    Timber.d("isLimitAdTrackingEnabled: true")
                }
            } catch (e: GooglePlayServicesNotAvailableException) {
                Timber.d(e)
            } catch (e: GooglePlayServicesRepairableException) {
                Timber.d(e)
            }
        }
    }

    fun fetchGlobalIPAddress() {
        viewModelScope.launch {
            val request = Request.Builder()
                .url("https://icanhazip.com/")
                .build()
            val result = withContext(Dispatchers.IO) {
                okHttpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.body?.string()
                    } else {
                        "failed/ code: ${response.code} / message: ${response.message}"
                    }
                }
            }
            _globalIPAddress.postValue(result?.replace("[\n\r]".toRegex(), ""))
        }
    }

    fun fetchLocalIPAddress() {
        val manager: ConnectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
                val value = linkProperties.linkAddresses.firstOrNull { it.address is Inet4Address }
                    .toString()
                _localIPAddress.postValue(value)
            }
        }
        manager.registerDefaultNetworkCallback(networkCallback)
    }

    fun requestWifiInfo() {
        // TODO: 位置情報のリクエスト
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            requestNetworkCapability(manager)
        } else {
            val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info: WifiInfo = manager.connectionInfo
            _ssid.postValue(info.ssid)
            _bssid.postValue(info.bssid)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestNetworkCapability(manager: ConnectivityManager) {
        val request = NetworkRequest.Builder()
            .addTransportType(TRANSPORT_WIFI)
            .build()

        // FLAG_INCLUDE_LOCATION_INFOを指定しないとWifiInfoが取得できない
        val callback = object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val wifi = (networkCapabilities.transportInfo as? WifiInfo) ?: return@onCapabilitiesChanged
                _ssid.postValue(wifi.ssid)
                _bssid.postValue(wifi.bssid)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val wifi = (manager.getNetworkCapabilities(network)?.transportInfo as? WifiInfo) ?: return@onAvailable
                _ssid.postValue(wifi.ssid)
                _bssid.postValue(wifi.bssid)
            }
        }
        manager.registerNetworkCallback(request, callback)
        manager.requestNetwork(request, callback)
    }
}