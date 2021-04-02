package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.liveData
import com.cxk.nwuhelper.ui.home.model.LoginPostBody
import com.cxk.nwuhelper.ui.home.model.SearchSessionsResponse
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchDevices(authorization: String) = liveData(Dispatchers.IO) {
        val result = try {
            val devicesResponse = NwuStudentNetwork.searchDevices(authorization)
            val sessionsList: List<SearchSessionsResponse.SearchSessions> = devicesResponse.sessions
            Result.success(sessionsList)
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun loginDevices(loginPostBody: LoginPostBody) = liveData(Dispatchers.IO) {
        val result = try {
            val loginResponse = NwuStudentNetwork.loginDevices(loginPostBody)
            Result.success(loginResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun deleteDevice(authorization:String,deviceId:String) = liveData(Dispatchers.IO) {
        val result = try {
            val deleteResponse = NwuStudentNetwork.deleteDevice(authorization,deviceId)
            Result.success(deleteResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

}