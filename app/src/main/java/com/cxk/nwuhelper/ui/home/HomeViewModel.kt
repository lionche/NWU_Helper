package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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







}