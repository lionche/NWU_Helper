package com.cxk.nwuhelper.ui.home

import android.app.AlertDialog
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.base.BaseVMFragment
import com.cxk.nwuhelper.ui.home.model.SearchSessionsResponse
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce

class HomeFragment : BaseVMFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun observerData() {
        binding.model = viewModel
        binding.lifecycleOwner = this

        viewModel.buttonState.observe(this, {
            when (it) {
                "wifi_available" -> {
                    binding.progressBar.visibility = GONE
                    binding.btnLogin.visibility = VISIBLE
                    binding.btnLogin.setIconResource(R.drawable.ic_baseline_arrow_forward_24)
                }
                "wifi_not_available" -> {
                    binding.progressBar.visibility = GONE
                    binding.btnLogin.visibility = VISIBLE
                    binding.btnLogin.setIconResource(R.drawable.ic_baseline_wifi_24)
                }
                "wrong_password" -> {
                    binding.progressBar.visibility = GONE
                    binding.btnLogin.visibility = VISIBLE
                    binding.btnLogin.setIconResource(R.drawable.ic_baseline_refresh_24)
                }
                "login_success" -> {
                    binding.progressBar.visibility = GONE
                    binding.btnLogin.visibility = GONE
                    binding.btnSuccess.apply {
                        visibility = VISIBLE
                        isChecked = true
                        isClickable = false
                    }


                }
            }
        })

        viewModel.deviceLiveData.observe(this, { result ->
            val sessionsList = result.getOrNull()
            sessionsList?.let {
                viewModel.deviceList = it as ArrayList<SearchSessionsResponse.Sessions>
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
                Log.d("test123", "token:$token")
                viewModel.authorization.value = token

                if (this.statusCode == 200) {
                    Log.d("test123", "登陆成功")
                    viewModel.buttonState.value = "login_success"
                } else {
                    this.errorDescription.apply {
                        when (this.first()) {
                            //pc already have 2 sessions
                            'p' -> {
                                Toast.makeText(requireActivity(), "已登陆2台设备", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            //invalid username or password
                            'i' -> {
                                Toast.makeText(requireActivity(), "用户名或密码错误", Toast.LENGTH_SHORT)
                                    .show()
                                viewModel.buttonState.value = "wrong_password"
                            }

                            //NAS no response
                            'N' -> {
                                Toast.makeText(requireActivity(), "是不是连错网了", Toast.LENGTH_SHORT)
                                    .show()
                                viewModel.buttonState.value = "wifi_not_available"

                            }

                        }

                    }
                    Log.d("test123", "登陆失败:原因:${this.errorDescription}")
                }
            }
        })
        viewModel.deleteLiveData.observe(this, { result ->
            result.getOrNull()?.let { Log.d("test123", "删除成功") }
                ?: let {
                    Log.d(
                        "test123",
                        "设备码或者token错误"
                    )
                }

        })

        //设置过度动画特效
        val doubleBounce: Sprite = DoubleBounce()
        binding.progressBar.setIndeterminateDrawable(doubleBounce)
    }

    override fun initEvent() {

        viewModel.netCheck()

        binding.mushroom.setOnClickListener {
//            Log.d("test123", "initEvent: 点击搜索设备")


            viewModel.authorization.value?.let {
                viewModel.searchDeviceLiveData.value = it
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("title")
                    setMessage("message")
                    setCancelable(false)
                    setPositiveButton("ok") { dialog, which ->
                    }
                    setNeutralButton("Cancel") { dialog, which ->
                    }
                    show()
                }
            }?:let {
                Toast.makeText(
                    requireContext(),
                    "请先登陆",
                    Toast.LENGTH_SHORT
                ).show() }
        }

        binding.mushroom.setOnLongClickListener {
            Log.d("test123", "initEvent: 点击删除设备")
            viewModel.authorization.value?.let {
                viewModel.deleteDevice(
                    it,
                    viewModel.deviceList[0].acct_unique_id
                )
            }
            true
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

