package com.cxk.nwuhelper.ui.wenet

import androidx.lifecycle.liveData
import com.cxk.nwuhelper.ui.wenet.model.LoginPostBody
import com.cxk.nwuhelper.ui.wenet.model.SearchSessionsResponse
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchDevices(authorization: String) = fire(Dispatchers.IO) {
        val devicesResponse = Wenetwork.searchDevices(authorization)
        val sessionsList: List<SearchSessionsResponse.Sessions> = devicesResponse.sessions
        Result.success(sessionsList)

    }

    fun loginDevices(loginPostBody: LoginPostBody) = fire(Dispatchers.IO) {
            val loginResponse = Wenetwork.loginDevices(loginPostBody)
            Result.success(loginResponse)
        }

    fun deleteDevice(authorization: String, deviceId: String) = fire(Dispatchers.IO) {
            val deleteResponse = Wenetwork.deleteDevice(authorization, deviceId)
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