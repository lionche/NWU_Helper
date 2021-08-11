package com.cxk.nwuhelper.ui.wenet

import android.app.AlertDialog
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentWenetBinding
import com.cxk.nwuhelper.ui.base.BaseVMPFragment
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.ui.wenet.model.SearchSessionsResponse
import com.cxk.nwuhelper.utils.AppPrefsUtils
import com.cxk.nwuhelper.utils.showToast
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import java.lang.Thread.sleep


class WenetFragment : BaseVMPFragment<FragmentWenetBinding, WenetViewModel>() {

    override fun onResume() {
        Log.d("test123", "onResume:检查网络 ")
        viewModel.netCheck()
        super.onResume()
    }

    override fun observerData() {
        binding.model = viewModel
        binding.lifecycleOwner = this


        viewModel.rmPasswordLiveData.observe(this, {
            when (it) {
                false -> viewModel.autoLoginLiveData.value = false
            }
        })
        viewModel.autoLoginLiveData.observe(this,
            {
                when (it) {
                    true -> {
                        viewModel.rmPasswordLiveData.value = true
                    }
                    false -> {
                        AppPrefsUtils.putBoolean(
                            BaseConstant.IS_AUTO_LOGIN_WENET,
                            viewModel.autoLoginLiveData.value!!
                        )
                        Log.d("gouxuan", "取消自动登陆")
                    }
                }
            })

        viewModel.buttonState.observe(this,
            {
                when (it) {
                    "wifi_available" -> {
                        binding.progressBar.visibility = GONE
                        binding.btnSuccess.visibility = GONE
                        binding.btnLogin.visibility = VISIBLE
                        binding.btnLogin.setIconResource(R.drawable.ic_baseline_arrow_forward_24)
                        viewModel.netAvailable = true
                        Log.d("password", "fragmentpost后: ${viewModel.netAvailable}")
                        viewModel.judgeEnable()

                        if (viewModel.autoLoginLiveData.value == true) {
                            viewModel.loginWenet()
                        }


                    }
                    "start_to_login" -> {
                        binding.btnLogin.visibility = GONE
                        binding.btnSuccess.visibility = GONE
                        binding.progressBar.visibility = VISIBLE
                    }
                    "wifi_not_available" -> {
                        binding.progressBar.visibility = GONE
                        binding.btnLogin.visibility = VISIBLE
                        binding.btnSuccess.visibility = GONE
                        binding.btnLogin.setIconResource(R.drawable.ic_baseline_wifi_24)
                        viewModel.netAvailable = false
                        viewModel.judgeEnable()
                    }
                    "wrong_password" -> {
                        binding.btnLogin.visibility = VISIBLE
                        binding.progressBar.visibility = GONE
                        binding.btnSuccess.visibility = GONE
                        binding.btnLogin.setIconResource(R.drawable.ic_baseline_refresh_24)
                    }
                    "2_devices" -> {
                        viewModel.searchDeviceLiveData.value = viewModel.authorization.value
                        binding.btnLogin.visibility = VISIBLE
                        binding.progressBar.visibility = GONE
                        binding.btnSuccess.visibility = GONE
                        binding.btnLogin.setIconResource(R.drawable.ic_baseline_refresh_24)
                    }
                    "login_success" -> {

                        binding.mushroom.setImageResource(R.drawable.mushroom)

                        AppPrefsUtils.putBoolean(
                            BaseConstant.IS_REMEMBER_PASSWORD_WENET,
                            viewModel.rmPasswordLiveData.value!!
                        )
                        AppPrefsUtils.putBoolean(
                            BaseConstant.IS_AUTO_LOGIN_WENET,
                            viewModel.autoLoginLiveData.value!!
                        )

                        /**
                         * 判断是否记住密码
                         */
                        if (viewModel.rmPasswordLiveData.value!!) {
                            AppPrefsUtils.putString(BaseConstant.NAME_WENET, viewModel.name)
                            AppPrefsUtils.putString(
                                BaseConstant.PASSWORD_WENET,
                                viewModel.password
                            )
                            Log.d(
                                "gouxuan",
                                "loginNwuStudent: 保存用户为${viewModel.name},密码为${viewModel.password}"
                            )
                        } else {
                            AppPrefsUtils.putString(BaseConstant.NAME_WENET, "")
                            AppPrefsUtils.putString(BaseConstant.PASSWORD_WENET, "")
                        }

                        binding.progressBar.visibility = GONE
                        binding.btnSuccess.visibility = VISIBLE
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
//                viewModel.deviceList = it as ArrayList<SearchSessionsResponse.Sessions>
                showDialog(it)
            } ?: let {
                Log.d("test123", "没有连接设备")
            }
        })
        viewModel.loginLiveData.observe(this,
            { result ->
                Log.d("test123", "$result")

                result.getOrNull()?.apply {
                    Log.d("test123", "token:$token")
                    viewModel.authorization.value = token

                    if (this.statusCode == 200) {
                        Log.d("test123", "登陆成功")
                        "登陆成功啦".showToast(requireContext())
                        viewModel.buttonState.value = "login_success"
                    } else {
                        this.errorDescription.apply {
                            when (this.first()) {
                                //pc already have 2 sessions
                                'p' -> {
//                                    "已登陆2台设备".showToast(requireContext())
                                    viewModel.buttonState.value = "2_devices"

                                }
                                //invalid username or password
                                'i' -> {

                                    "用户名或密码错误".showToast(requireContext())

                                    viewModel.buttonState.value = "wrong_password"
                                }

                                //NAS no response
                                'N' -> {
                                    "是不是连错网了".showToast(requireContext())
                                    viewModel.buttonState.value = "wifi_not_available"

                                }
                                //authentication rejected
                                //删除了设备马上重新登陆会出现这个问题
                                'a' -> {
//                                    "等会再登陆".showToast(requireContext())
                                    sleep(800)
                                    viewModel.loginWenet()

                                }
                                //Failed to login
                                'F' -> {
                                    "Failed to login".showToast(requireContext())
                                    viewModel.buttonState.value = "wrong_password"

                                }

                            }

                        }
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

        //设置过度动画特效
        val doubleBounce: Sprite = DoubleBounce()
        binding.progressBar.setIndeterminateDrawable(doubleBounce)
    }

//    override fun initEvent() {
        /**
         * 检测是否连接校园网
         */
//        viewModel.netCheck()

        /**
         * 检测是否为自动登录
         */


//        binding.mushroom.setOnClickListener {
//
//            //保存用户名密码
//            val rm = AppPrefsUtils.getBoolean(BaseConstant.IS_REMEMBER_PASSWORD_WENET)
//            val auto = AppPrefsUtils.getBoolean(BaseConstant.IS_AUTO_LOGIN_WENET)
//            val name = AppPrefsUtils.getString(BaseConstant.NAME_WENET)
//            val password = AppPrefsUtils.getString(BaseConstant.PASSWORD_WENET)
//            Log.d("gouxuan", "是否记住密码$rm,是否自动登录$auto,用户名$name,密码$password")
////            viewModel.authorization.value?.let {
////                viewModel.searchDeviceLiveData.value = it
////            } ?: let {
////                "请先登陆".showToast(requireContext())
////            }
//        }
//
//
////        binding.mushroom.setOnLongClickListener {
////            Log.d("test123", "initEvent: 点击删除设备")
////            viewModel.authorization.value?.let {
//////                viewModel.deleteDevice(
//////                    it,
//////                    viewModel.deviceList[0].acct_unique_id
//////                )
////            }
////            true
////        }
////        binding.btnLogin.setOnClickListener {
////            Log.d("test123", "initEvent: 点击登录")
////            viewModel.loginDevices(viewModel.loginPostBody)
////
////        }
////        binding.btnDelete.setOnClickListener {
////            Log.d("test123", "initEvent: 点击删除")
////            viewModel.deleteDevice(viewModel.authorization, viewModel.deviceId)
////        }
//    }


    private fun showDialog(list: List<SearchSessionsResponse.Sessions>) {
        val loginDevices = list.map { it.deviceType }.toTypedArray()

        AlertDialog.Builder(requireContext()).apply {
            setTitle("放弃设备")
            setCancelable(false)
            setNeutralButton("离开") { dialog, which -> }

            for (item in loginDevices) {
                Log.e("test123", "showDialog: $item")
            }
            val selectDevices: MutableList<Int> = ArrayList()
            setMultiChoiceItems(loginDevices, null) { dialog, which, isChecked ->
                if (isChecked) {
                    selectDevices.add(which)
                } else {
                    selectDevices.remove(which)
                }
            }
            setPositiveButton("确认") { dialog, which ->
                for (deviceIndex in selectDevices) {
                    viewModel.deleteDevice(
                        viewModel.authorization.value!!, list.map { it.acct_unique_id }[deviceIndex]
                    )
                    Log.e("test123", "选择放弃的设备：${loginDevices[deviceIndex]}")
                }
                if (selectDevices.size != 0) {
                    viewModel.loginWenet()
                }


            }
            show()
        }

    }


    override fun getSubLayoutId() = R.layout.fragment_wenet
    override fun getSubVMClass() = WenetViewModel::class.java
    override fun initViewModel() {
        val IS_AUTO_LOGIN = AppPrefsUtils.getBoolean(BaseConstant.IS_AUTO_LOGIN_WENET)
        val IS_REMEMBER_PASSWORD =
            AppPrefsUtils.getBoolean(BaseConstant.IS_REMEMBER_PASSWORD_WENET)
        val NAME_STUDENT = AppPrefsUtils.getString(BaseConstant.NAME_WENET)
        val PASSWORD_STUDENT = AppPrefsUtils.getString(BaseConstant.PASSWORD_WENET)

        val homeSpBean =
            NetSpBean(NAME_STUDENT!!, PASSWORD_STUDENT!!, IS_REMEMBER_PASSWORD, IS_AUTO_LOGIN)
        viewModel = ViewModelProvider(this, WenetViewModelFactory(homeSpBean)).get(getSubVMClass())
    }


}

