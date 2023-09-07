package com.example.yusakumaki.functionconfirm.fragments

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yusakumaki.functionconfirm.entity.TerminalInfoData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.Inet4Address

class TerminalInfoViewModel(application: Application) : AndroidViewModel(application) {

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

    fun onCreate() {
        viewModelScope.launch {
            val advertisingId = requestAdvertisingId()
//            advertisingId.collect {
//                _advertisingId.postValue(it)
//            }

            val globalIPAddress = getGlobalIPAddress()
//            globalIPAddress.collect {
//                _globalIPAddress.postValue(it)
//            }

            val localIPAddress = getLocalIPAddress()
//            localIPAddress.collect {
//                _localIPAddress.postValue(it)
//            }

            val wifiInfo = requestWifiInfo()

            advertisingId
                .zip(globalIPAddress) { a, b -> a to b }
                .zip(localIPAddress) { a, b -> Triple(a.first, a.second, b) }
                .zip(wifiInfo) { a, b ->
                    TerminalInfoData(a.first, a.second, a.third, b?.ssid, b?.bssid)
                }
                .collect {
                    _advertisingId.postValue(it.advertisingId)
                    _globalIPAddress.postValue(it.globalIPAddress)
                    _localIPAddress.postValue(it.localIPAddress)
                    _ssid.postValue(it.ssid)
                    _bssid.postValue(it.bssid)
                }

        }
    }

    private suspend fun requestAdvertisingId() = flow {
        kotlin.runCatching {
            AdvertisingIdClient.getAdvertisingIdInfo(context).id
        }.onSuccess {
            emit(it)
        }.onFailure {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getGlobalIPAddress() = flow {
        kotlin.runCatching {
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
            result?.replace("[\n\r]".toRegex(), "")
        }.onSuccess {
            emit(it)
        }.onFailure {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getLocalIPAddress() = callbackFlow {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties)
                val value = linkProperties.linkAddresses
                    .firstOrNull { it.address is Inet4Address }
                    ?.toString()
                trySend(value)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(null)
            }
        }
        manager.registerDefaultNetworkCallback(networkCallback)
        awaitClose { manager.unregisterNetworkCallback(networkCallback) }
    }

    private suspend fun requestWifiInfo() = callbackFlow {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()

            // FLAG_INCLUDE_LOCATION_INFOを指定しないとWifiInfoが取得できない
            val callback = object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    trySend(networkCapabilities.transportInfo as? WifiInfo)
                }
            }
            manager.registerNetworkCallback(request, callback)
            awaitClose { manager.unregisterNetworkCallback(callback) }
        } else {
            val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            @Suppress("DEPRECATION")
            trySend(manager?.connectionInfo)
        }
    }
}