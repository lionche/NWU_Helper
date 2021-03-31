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


//    suspend fun getStudentDevice() {
//        _deviceList.value = ServiceCreator.create<SearchConnectDeviceService>()
//            .getDeviceList("a9e32cf5061f67602f3745170ca37381ad22449939e7dfd905ac583a57cc79d1d67fb2a5d5218769017b566f9ca0c74a15b92f4094c57ae9")
//            .await()
//
//    }


}