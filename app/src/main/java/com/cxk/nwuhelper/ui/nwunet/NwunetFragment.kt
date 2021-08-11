package com.cxk.nwuhelper.ui.nwunet

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentNwunetBinding
import com.cxk.nwuhelper.ui.base.BaseVMPFragment
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.AppPrefsUtils
import com.cxk.nwuhelper.utils.showToast
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce


class NwunetFragment : BaseVMPFragment<FragmentNwunetBinding, NwunetViewModel>() {

    override fun onResume() {
        Log.d("test123", "onResume: 网络检查")
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
        viewModel.autoLoginLiveData.observe(this, {
            when (it) {
                true -> {
                    viewModel.rmPasswordLiveData.value = true
                }
                false -> {
                    AppPrefsUtils.putBoolean(
                        BaseConstant.IS_AUTO_LOGIN_NWUNET,
                        viewModel.autoLoginLiveData.value!!
                    )
                    Log.d("gouxuan", "取消自动登陆")
                }
            }
        })

        viewModel.buttonState.observe(this, {
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
                        viewModel.loginNwunet()
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
                "login_success" -> {

                    binding.mushroom.setImageResource(R.drawable.mushroom)


                    AppPrefsUtils.putBoolean(
                        BaseConstant.IS_REMEMBER_PASSWORD_NWUNET,
                        viewModel.rmPasswordLiveData.value!!
                    )
                    AppPrefsUtils.putBoolean(
                        BaseConstant.IS_AUTO_LOGIN_NWUNET,
                        viewModel.autoLoginLiveData.value!!
                    )

                    /**
                     * 判断是否记住密码
                     */
                    if (viewModel.rmPasswordLiveData.value!!) {
                        AppPrefsUtils.putString(BaseConstant.NAME_NWUNET, viewModel.name)
                        AppPrefsUtils.putString(
                            BaseConstant.PASSWORD_NWUNET,
                            viewModel.password
                        )
                        Log.d(
                            "gouxuan",
                            "loginNwuStudent: 保存用户为${viewModel.name},密码为${viewModel.password}"
                        )
                    } else {
                        AppPrefsUtils.putString(BaseConstant.NAME_NWUNET, "")
                        AppPrefsUtils.putString(BaseConstant.PASSWORD_NWUNET, "")
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

        viewModel.loginLiveData.observe(this, { result ->
            result.getOrNull()?.apply {
                viewModel.loginResponseLiveData.value = this.string()
            }
        })

        viewModel.loginResponseLiveData.observe(this, { result ->
            Log.d("nwutest", "$result")

            if ("认证成功页" in result) {
                Log.d("nwutest", "登陆成功啦")
                "登陆成功啦".showToast(requireContext())
                viewModel.buttonState.value = "login_success"
            } else {
                viewModel.buttonState.value = "wrong_password"
                "用户名或密码错误".showToast(requireContext())
                Log.d("nwutest", "用户名或密码错误")
            }
        })


        //设置过度动画特效
        val doubleBounce: Sprite = DoubleBounce()
        binding.progressBar.setIndeterminateDrawable(doubleBounce)
    }

//    override fun initEvent() {


//        binding.mushroom.setOnClickListener {
//
//            //保存用户名密码
//            val rm = AppPrefsUtils.getBoolean(BaseConstant.IS_REMEMBER_PASSWORD_NWUNET)
//            val auto = AppPrefsUtils.getBoolean(BaseConstant.IS_AUTO_LOGIN_NWUNET)
//            val name = AppPrefsUtils.getString(BaseConstant.NAME_NWUNET)
//            val password = AppPrefsUtils.getString(BaseConstant.PASSWORD_NWUNET)
//            Log.d("gouxuan", "是否记住密码$rm,是否自动登录$auto,用户名$name,密码$password")
////            viewModel.authorization.value?.let {
////                viewModel.searchDeviceLiveData.value = it
////            } ?: let {
////                "请先登陆".showToast(requireContext())
////            }
//        }


//        binding.mushroom.setOnLongClickListener {
//            Log.d("test123", "initEvent: 点击删除设备")
//            viewModel.authorization.value?.let {
////                viewModel.deleteDevice(
////                    it,
////                    viewModel.deviceList[0].acct_unique_id
////                )
//            }
//            true
//        }
//        binding.btnLogin.setOnClickListener {
//            Log.d("test123", "initEvent: 点击登录")
//            viewModel.loginDevices(viewModel.loginPostBody)
//
//        }
//        binding.btnDelete.setOnClickListener {
//            Log.d("test123", "initEvent: 点击删除")
//            viewModel.deleteDevice(viewModel.authorization, viewModel.deviceId)
//        }
//    }


    override fun getSubLayoutId() = R.layout.fragment_nwunet
    override fun getSubVMClass() = NwunetViewModel::class.java
    override fun initViewModel() {
        val IS_AUTO_LOGIN = AppPrefsUtils.getBoolean(BaseConstant.IS_AUTO_LOGIN_NWUNET)
        val IS_REMEMBER_PASSWORD =
            AppPrefsUtils.getBoolean(BaseConstant.IS_REMEMBER_PASSWORD_NWUNET)
        val NAME_NWUNET = AppPrefsUtils.getString(BaseConstant.NAME_NWUNET)
        val PASSWORD_NWUNET = AppPrefsUtils.getString(BaseConstant.PASSWORD_NWUNET)

        val NwunetSpBean =
            NetSpBean(NAME_NWUNET!!, PASSWORD_NWUNET!!, IS_REMEMBER_PASSWORD, IS_AUTO_LOGIN)
        viewModel =
            ViewModelProvider(this, NwunetViewModelFactory(NwunetSpBean)).get(getSubVMClass())
    }


}

