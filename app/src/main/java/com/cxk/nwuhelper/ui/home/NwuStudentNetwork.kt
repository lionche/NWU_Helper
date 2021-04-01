package com.cxk.nwuhelper.ui.home

import com.cxk.nwuhelper.ui.home.model.LoginPostBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NwuStudentNetwork {

    private val nwuStudentService = ServiceCreator.create<NwuStudentService>()


    suspend fun searchDevices(authorization:String) = nwuStudentService.searchDevices(authorization).await()
    suspend fun loginDevices(loginPostBody: LoginPostBody) = nwuStudentService.loginDevice(loginPostBody).await()
    suspend fun deleteDevice(authorization:String,deviceId:String) = nwuStudentService.deleteDevice(authorization,deviceId).await()


    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("respon body is null")
                    )
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}

