package com.cxk.nwuhelper.ui.home

import android.util.Log
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.base.BaseVMFragment
import com.cxk.nwuhelper.ui.home.model.SearchSessionsResponse

class HomeFragment : BaseVMFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun observerData() {

        binding.lifecycleOwner = this

        viewModel.deviceLiveData.observe(this, { result ->
            val sessionsList = result.getOrNull()
            sessionsList?.let {
                viewModel.deviceList = it as ArrayList<SearchSessionsResponse.SearchSessions>
                for (session in viewModel.deviceList) {
                    Log.d(
                        "test123",
                        "\n设备类型: ${session.deviceType}\nip地址:${session.framed_ip_address}\nmac地址：${session.calling_station_id}\n设备码:${session.acct_unique_id}"
                    )
                }
            } ?: let {
                Log.d("test123", "没有连接设备")
            }
        })
        viewModel.loginLiveData.observe(this, { result ->
            result.getOrNull()?.apply {
                if (this.statusCode == 200) {
                    Log.d("test123", "登陆成功,token:${this.token}")
                } else {
                    Log.d("test123", "登陆失败:原因:${this.errorDescription}")
                }
            }
        })
        viewModel.deleteLiveData.observe(this,
            { result ->
                result.getOrNull()?.let { Log.d("test123", "删除成功") }
                    ?: let {
                        Log.d(
                            "test123",
                            "设备码或者token错误"
                        )
                    }

            })
    }

    override fun initEvent() {
        binding.mushroom.setOnClickListener {
            Log.d("test123", "initEvent: 点击搜索设备")
            viewModel.searchDevices(viewModel.authorization)

        }
//        binding.btnLogin.setOnClickListener {
//            Log.d("test123", "initEvent: 点击登录")
//            viewModel.loginDevices(viewModel.loginPostBody)
//
//        }
//        binding.btnDelete.setOnClickListener {
//            Log.d("test123", "initEvent: 点击删除")
//            viewModel.deleteDevice(viewModel.authorization, viewModel.deviceId)
//        }
    }

    override fun getSubLayoutId() = R.layout.fragment_home
    override fun getSubVMClass() = HomeViewModel::class.java


}

