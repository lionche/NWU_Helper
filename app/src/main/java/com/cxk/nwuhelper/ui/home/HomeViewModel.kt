package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cxk.nwuhelper.ui.home.model.LoginPostBody
import com.cxk.nwuhelper.ui.home.model.SearchSessions

class HomeViewModel : ViewModel() {

    private var searchDeviceLiveData = MutableLiveData<String>()

    var deviceList = ArrayList<SearchSessions>()

    val deviceLiveData = Transformations.switchMap(searchDeviceLiveData){authorization ->
        Repository.searchDevices(authorization)
    }
    fun searchDevices(authorization: String) {
        searchDeviceLiveData.value = authorization
    }

    val authorization = "a9e32cf5061f67602f3745170ca37381ad22449939e7dfd97f30e79e8a129527d17e1a7b6a96df6062ee34592c9742771c8864ecc74a8708"



    //################################################################################
    //################################################################################
    //################################################################################
    val loginPostBody = LoginPostBody(
        redirectUrl = "http://10.16.0.12:8081/?usermac=XX:XX:XX:XX:XX:XX&userip=10.17.65.9&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1",
        webAuthPassword = "09005X",
        webAuthUser = "202032908"
    )

    private var loginDeviceLiveData = MutableLiveData<LoginPostBody>()

    val loginLiveData = Transformations.switchMap(loginDeviceLiveData){loginPostBody ->
        Repository.loginDevices(loginPostBody)
    }
    fun loginDevices(loginPostBody: LoginPostBody) {
        loginDeviceLiveData.value = loginPostBody
    }







}