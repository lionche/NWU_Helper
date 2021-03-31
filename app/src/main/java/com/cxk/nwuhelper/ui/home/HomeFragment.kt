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
            val sessionsList :ArrayList<SearchSessions> = result.getOrNull() as ArrayList<SearchSessions>
            viewModel.deviceList= sessionsList
            for (session in viewModel.deviceList) {
                Log.d("test123", "\n设备类型: ${session.deviceType}\nip地址:${session.framed_ip_address}\nmac地址：${session.calling_station_id}\n设备码:${session.acct_unique_id}")
            }
        })

    }

    override fun initEvent() {
        binding.button.setOnClickListener {
            viewModel.searchDevices("a9e32cf5061f67602f3745170ca37381ad22449939e7dfd97f30e79e8a129527d17e1a7b6a96df6062ee34592c9742771c8864ecc74a8708")
        }
    }


    override fun getSubLayoutId() = R.layout.fragment_home
    override fun getSubVMClass() = HomeViewModel::class.java


}

