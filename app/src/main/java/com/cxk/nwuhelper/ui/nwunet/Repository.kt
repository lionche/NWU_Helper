package com.cxk.nwuhelper.ui.nwunet


import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Repository {

    fun loginDevices(name :String, password :String) = fire(Dispatchers.IO) {
            val loginResponse = Nwunetwork.loginDevices(name,password)
            Result.success(loginResponse)
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