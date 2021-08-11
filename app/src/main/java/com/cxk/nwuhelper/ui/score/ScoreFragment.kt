package com.cxk.nwuhelper.ui.score

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.ScoreActivity
import com.cxk.nwuhelper.databinding.FragmentDashboardBinding
import com.cxk.nwuhelper.ui.base.BaseVMPFragment
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.AppPrefsUtils
import com.cxk.nwuhelper.utils.SerializableMap
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce


class ScoreFragment : BaseVMPFragment<FragmentDashboardBinding, ScoreViewModel>() {

    override fun observerData() {
        binding.model = viewModel

        binding.lifecycleOwner = this

        viewModel.judgeEnable()


        viewModel.resultMap.observe(this, {
            it.let {
                val intent = Intent(context, ScoreActivity::class.java)

                val myMap = SerializableMap()
                myMap.map = it//将map数据添加到封装的myMap中
                val bundle =  Bundle()
                bundle.putSerializable("map", myMap)
                intent.putExtras(bundle)

                requireActivity().startActivity(intent)
            }
        }
        )

        viewModel.buttonState.observe(this,
            {
                when (it) {
                    "start_to_login" -> {
                        binding.btnLogin.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnSuccess.visibility = View.GONE
                    }
                    "wrong_password" -> {
                        binding.btnLogin.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.btnSuccess.visibility = View.GONE
                        binding.btnLogin.setIconResource(R.drawable.ic_baseline_refresh_24)
                    }
                    "login_success" -> {
                        binding.mushroom.setImageResource(R.drawable.mushroom)

                        AppPrefsUtils.putBoolean(
                            BaseConstant.IS_REMEMBER_PASSWORD_SCORE,
                            viewModel.rmPasswordLiveData.value!!
                        )
                        AppPrefsUtils.putBoolean(
                            BaseConstant.IS_AUTO_LOGIN_SCORE,
                            viewModel.autoLoginLiveData.value!!
                        )

                        /**
                         * 判断是否记住密码
                         */
                        if (viewModel.rmPasswordLiveData.value!!) {
                            AppPrefsUtils.putString(BaseConstant.NAME_SCORE, viewModel.name)
                            AppPrefsUtils.putString(
                                BaseConstant.PASSWORD_SCORE,
                                viewModel.password
                            )
                            Log.d(
                                "gouxuan",
                                "loginNwuStudent: 保存用户为${viewModel.name},密码为${viewModel.password}"
                            )
                        } else {
                            AppPrefsUtils.putString(BaseConstant.NAME_SCORE, "")
                            AppPrefsUtils.putString(BaseConstant.PASSWORD_SCORE, "")
                        }

                        binding.progressBar.visibility = View.GONE
                        binding.btnSuccess.visibility = View.VISIBLE
                        binding.btnSuccess.apply {
                            visibility = View.VISIBLE
                            isChecked = true
                            isClickable = false
                        }


                    }
                }
            })

        //设置过度动画特效
        val doubleBounce: Sprite = DoubleBounce()
        binding.progressBar.setIndeterminateDrawable(doubleBounce)


    }



    override fun getSubLayoutId() = R.layout.fragment_dashboard
    override fun getSubVMClass() = ScoreViewModel::class.java

    override fun initViewModel() {
        val IS_AUTO_LOGIN = AppPrefsUtils.getBoolean(BaseConstant.IS_AUTO_LOGIN_SCORE)
        val IS_REMEMBER_PASSWORD =
            AppPrefsUtils.getBoolean(BaseConstant.IS_REMEMBER_PASSWORD_SCORE)
        val NAME_SCORE = AppPrefsUtils.getString(BaseConstant.NAME_SCORE)
        val PASSWORD_SCORE = AppPrefsUtils.getString(BaseConstant.PASSWORD_SCORE)

        val ScoreSpBean =
            NetSpBean(NAME_SCORE!!, PASSWORD_SCORE!!, IS_REMEMBER_PASSWORD, IS_AUTO_LOGIN)

        viewModel =
            ViewModelProvider(this, ScoreViewModelFactory(ScoreSpBean)).get(getSubVMClass())
    }


}