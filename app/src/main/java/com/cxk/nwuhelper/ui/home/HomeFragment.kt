package com.cxk.nwuhelper.ui.home

import android.util.Log
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.base.BaseVMFragment
import com.cxk.nwuhelper.ui.home.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : BaseVMFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun observerData() {

        binding.lifecycleOwner = this

        viewModel.deviceLiveData.observe(this, { result ->
            val sessionsList = result.getOrNull()
            if (sessionsList != null) {
                viewModel.deviceList = sessionsList as ArrayList<SearchSessions>
                for (session in viewModel.deviceList) {
                    Log.d(
                        "test123",
                        "\n设备类型: ${session.deviceType}\nip地址:${session.framed_ip_address}\nmac地址：${session.calling_station_id}\n设备码:${session.acct_unique_id}"
                    )
                }
            } else {
                Log.d("test123", "没有连接设备")
            }
        })
    }

    override fun initEvent() {

        val loginPostBody = LoginPostBody(
            redirectUrl = "http://10.16.0.12:8081/?usermac=A0:99:9B:05:CB:2B&userip=10.17.65.9&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1",
            webAuthPassword = "09005X",
            webAuthUser = "202032908"
        )
//
//        Log.d("test123", "initEvent: ${loginPostBody.redirectUrl.length}")
//
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseConstant.NWU_STUDENT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NwuStudentService::class.java)

        service.getResult(loginPostBody).enqueue(object : Callback<LoginFailResponse> {
            override fun onResponse(
                call: Call<LoginFailResponse>,
                response: Response<LoginFailResponse>
            ) {
                Log.d("test123", "onResponse: ${response.body()}")
            }

            override fun onFailure(call: Call<LoginFailResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })


//        viewModel.searchDevices("a9e32cf5061f67602f3745170ca37381ad22449939e7dfd97f30e79e8a129527d17e1a7b6a96df6062ee34592c9742771c8864ecc74a8708")
    }

    override fun getSubLayoutId() = R.layout.fragment_home
    override fun getSubVMClass() = HomeViewModel::class.java


}

