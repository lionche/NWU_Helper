package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.liveData
import com.cxk.nwuhelper.ui.home.model.LoginPostBody
import com.cxk.nwuhelper.ui.home.model.SearchSessionsResponse
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchDevices(authorization: String) = fire(Dispatchers.IO) {
        val devicesResponse = NwuStudentNetwork.searchDevices(authorization)
        val sessionsList: List<SearchSessionsResponse.Sessions> = devicesResponse.sessions
        Result.success(sessionsList)

    }

    fun loginDevices(loginPostBody: LoginPostBody) = fire(Dispatchers.IO) {
            val loginResponse = NwuStudentNetwork.loginDevices(loginPostBody)
            Result.success(loginResponse)
        }

    fun deleteDevice(authorization: String, deviceId: String) = fire(Dispatchers.IO) {
            val deleteResponse = NwuStudentNetwork.deleteDevice(authorization, deviceId)
            Result.success(deleteResponse)
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}