package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.liveData
import com.cxk.nwuhelper.ui.home.model.DeviceResponse
import com.cxk.nwuhelper.ui.home.model.Session
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchDevices(authorization: String) = liveData(Dispatchers.IO) {
        val result = try {
        val devicesResponse = NwuStudentNetwork.searchDevices(authorization)
        val sessionsList: List<Session> = devicesResponse.sessions
        Result.success(sessionsList)
    } catch (e: Exception) {
        Result.failure<DeviceResponse>(e)
    }
        emit(result)
    }


}