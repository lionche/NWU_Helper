package com.cxk.nwuhelper.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.che.haccp.ui.base.BaseViewFragment
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.base.ServiceCreator
import com.cxk.nwuhelper.ui.home.model.DeviceList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BaseViewFragment<FragmentHomeBinding>() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.button.setOnClickListener {
            val searchConnectDeviceService = ServiceCreator.create<SearchConnectDeviceService>()
            searchConnectDeviceService.getDeviceList("a9e32cf5061f67602f3745170ca37381ad22449939e7dfd905ac583a57cc79d1d67fb2a5d5218769017b566f9ca0c74a15b92f4094c57ae9")
                .enqueue(object : Callback<DeviceList> {
                    override fun onResponse(
                        call: Call<DeviceList>,
                        response: Response<DeviceList>
                    ) {
                        response.body()?.sessions?.let{
                            for (session in  it){
                                Log.d("test123", "" + session)
                            }
                        }
                    }
                    override fun onFailure(call: Call<DeviceList>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()


    }

    override fun getSubLayoutId() = R.layout.fragment_home
}


