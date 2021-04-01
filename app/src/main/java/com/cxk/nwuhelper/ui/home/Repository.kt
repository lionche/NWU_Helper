package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.liveData
import com.cxk.nwuhelper.ui.home.model.LoginPostBody
import com.cxk.nwuhelper.ui.home.model.SearchSessions
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchDevices(authorization: String) = liveData(Dispatchers.IO) {
        val result = try {
        val devicesResponse = NwuStudentNetwork.searchDevices(authorization)
        val sessionsList: List<SearchSessions> = devicesResponse.searchSessions
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

}