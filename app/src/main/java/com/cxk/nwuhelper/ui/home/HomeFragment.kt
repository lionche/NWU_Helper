package com.cxk.nwuhelper.ui.home

import android.util.Log
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.base.BaseVMFragment
import com.cxk.nwuhelper.ui.home.model.Session

class HomeFragment : BaseVMFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun observerData() {

        binding.lifecycleOwner = this

        viewModel.deviceLiveData.observe(this, { result ->
            val sessionsList :ArrayList<Session> = result.getOrNull() as ArrayList<Session>
            viewModel.deviceList= sessionsList
            for (session in viewModel.deviceList) {
                Log.d("test123", "\n设备类型: ${session.deviceType}\nip地址:${session.framed_ip_address}\nmac地址：${session.calling_station_id}\n设备码:${session.acct_unique_id}")
            }
        })

    }

    override fun initEvent() {
        binding.button.setOnClickListener {
//            viewModel.searchDevices("a9e32cf5061f67602f3745170ca37381ad22449939e7dfd905ac583a57cc79d1d67fb2a5d5218769017b566f9ca0c74a15b92f4094c57ae9")
            viewModel.searchDevices("a9e32cf5061f67602f3745170ca37381ad22449939e7dfd97f30e79e8a1295277c790b4339e74b17a3590857d744508886c4eb3033c9043c")
        }
    }


    override fun getSubLayoutId() = R.layout.fragment_home
    override fun getSubVMClass() = HomeViewModel::class.java


}

