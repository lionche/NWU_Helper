package com.cxk.nwuhelper.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.che.haccp.ui.base.BaseViewFragment
import com.cxk.nwuhelper.BaseConstant.LOGIN_URL_BASE
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : BaseViewFragment<FragmentHomeBinding>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.button.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(LOGIN_URL_BASE)
//                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val appService = retrofit.create(AppService::class.java)
            appService.getAppData().enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val webString = response.body()
                    if (webString != null) {
                        Log.d("webString", "onResponse: $webString")
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()

    }

    override fun getSubLayoutId() = R.layout.fragment_home
}


