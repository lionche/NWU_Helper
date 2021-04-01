package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cxk.nwuhelper.ui.home.model.LoginPostBody
import com.cxk.nwuhelper.ui.home.model.SearchSessions

class HomeViewModel : ViewModel() {

    private var searchDeviceLiveData = MutableLiveData<String>()

    var deviceList = ArrayList<SearchSessions>()

    val deviceLiveData = Transformations.switchMap(searchDeviceLiveData) { authorization ->
        Repository.searchDevices(authorization)
    }

    fun searchDevices(authorization: String) {
        searchDeviceLiveData.value = authorization
    }

    val authorization =
        "c1a3ef1ddf30bc41373b19bbb830c470f7522a72bff5057e57f92dea92f661ed1a47143bb07c36c5ffc1f238c68109202c1f59482b215a0e"


    //##########################################################################################################################################################
    //##########################################################################################################################################################
    val loginPostBody = LoginPostBody(
        redirectUrl = "http://10.16.0.12:8081/?usermac=XX:XX:XX:XX:XX:XX&userip=10.17.93.45&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1",
        webAuthPassword = "09005X",
        webAuthUser = "202032908"
    )

    private var loginDeviceLiveData = MutableLiveData<LoginPostBody>()

    val loginLiveData = Transformations.switchMap(loginDeviceLiveData) { loginPostBody ->
        Repository.loginDevices(loginPostBody)
    }

    fun loginDevices(loginPostBody: LoginPostBody) {
        loginDeviceLiveData.value = loginPostBody
    }

    //##########################################################################################################################################################
    //##########################################################################################################################################################


    val deviceId =
        "radius:acct:35cb8667-2649-4af3-86b7-854194219302:xx:b1bb841cbb342222ce6eb592d6cb6185"
    private val deleteDeviceLiveData = MutableLiveData<MutableMap<String, String>>()

    private val deleteMap: MutableMap<String, String> = HashMap()

    val deleteLiveData = Transformations.switchMap(deleteDeviceLiveData) {
        val authorization = it[authorization]
        val deviceId = it[deviceId]
        Repository.deleteDevice(authorization!!, deviceId!!)
    }

    fun deleteDevice(authorization: String, deviceId: String) {
        deleteMap[authorization] = authorization
        deleteMap[deviceId] = deviceId
        deleteDeviceLiveData.value = deleteMap
    }


}