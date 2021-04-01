package com.cxk.nwuhelper.ui.home

import android.util.Log
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.base.BaseVMFragment
import com.cxk.nwuhelper.ui.home.model.SearchSessions

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
        viewModel.loginLiveData.observe(this, { result ->
            val loginFailResponse = result.getOrNull()
            Log.d("test123","$loginFailResponse" )
        })
    }

    override fun initEvent() {
        binding.btnSearch.setOnClickListener{
            Log.d("test123", "initEvent: 点击搜索设备")
            viewModel.searchDevices(viewModel.authorization)
        }
        binding.btnLogin.setOnClickListener{
            Log.d("test123", "initEvent: 点击登录")
            viewModel.loginDevices(viewModel.loginPostBody)
        }
    }

    override fun getSubLayoutId() = R.layout.fragment_home
    override fun getSubVMClass() = HomeViewModel::class.java


}

