package com.cxk.nwuhelper.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.che.haccp.ui.base.BaseViewFragment
import com.cxk.nwuhelper.BaseConstant.SearchConnectDevice_URL
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.home.model.DeviceList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : BaseViewFragment<FragmentHomeBinding>() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.button.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(SearchConnectDevice_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val searchConnectDeviceService = retrofit.create(SearchConnectDeviceService::class.java)
            searchConnectDeviceService.getDeviceList().enqueue(object : Callback<DeviceList> {
                override fun onResponse(call: Call<DeviceList>, response: Response<DeviceList>) {
                    val response1 = response.body()

                    if (response1 != null) {
                        Log.d("test123", "onResponse: " + response1.sessions)
                    }

//                    val response = response.body()
//                    Log.d("test123", "onResponse: ,id is $response")
                }

                override fun onFailure(call: Call<DeviceList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()


/*        val repository = Repository()
        val viewModelFactory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.getPost()
        homeViewModel.myResponse.observe(viewLifecycleOwner,  { response ->
            Log.d("test123", "onActivityCreated: ", response.title)
        })*/
    }

    override fun getSubLayoutId() = R.layout.fragment_home
}


