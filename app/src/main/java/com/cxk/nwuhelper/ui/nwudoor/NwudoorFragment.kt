package com.cxk.nwuhelper.ui.nwudoor

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentNwudoorBinding
import com.cxk.nwuhelper.ui.base.BaseVMPFragment
import com.cxk.nwuhelper.ui.nwudoor.score.model.ScoreData
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.AppPrefsUtils
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce


class NwudoorFragment : BaseVMPFragment<FragmentNwudoorBinding, NwudoorViewModel>() {

    companion object {
        var scoreList = ArrayList<ScoreData>()
    }

    override fun observerData() {

        Log.d(
            "chengji",
            "导航到成绩0${viewModel.buttonState.value}"
        )

        binding.model = viewModel

        binding.lifecycleOwner = this

        viewModel.judgeEnable()

        viewModel.rmPasswordLiveData.observe(this) {
            when (it) {
                false -> viewModel.autoLoginLiveData.value = false
            }
        }

        viewModel.enable.observe(this) {
            when (it) {
                true -> {
                    if (viewModel.autoLoginLiveData.value == true) {
                        viewModel.visitWebsiteNewThread()
                    }
                }
            }
        }
        viewModel.autoLoginLiveData.observe(
            this
        ) {
            when (it) {
                true -> {
                    viewModel.rmPasswordLiveData.value = true
                }
                false -> {
                    AppPrefsUtils.putBoolean(
                        BaseConstant.IS_AUTO_LOGIN_SCORE,
                        viewModel.autoLoginLiveData.value!!
                    )
                    Log.d("gouxuan", "取消自动登陆")
                }
            }
        }




        viewModel.scoreFragmentState.observe(this) {
            when (it) {
                "open" -> {

                }
            }
        }

        viewModel.buttonState.observe(
            this
        ) {
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
//

                    Handler(Looper.getMainLooper()).postDelayed({
                        //                            把viewmodel的东西取出放入list
                        try {
                            scoreList = viewModel.scoreListLiveData.value!!
                        } catch (e: Exception) {
                        }

//                        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
//                            .navigate(R.id.action_navigation_nwudoor_to_scroeFragment)

                        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
                            .navigate(R.id.action_navigation_nwudoor_to_showPdfFragment)


                    }, 800)



                    binding.btnLogin.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.btnSuccess.apply {
                        visibility = View.VISIBLE
                        isChecked = true
                        isClickable = false
                    }
                }
            }
        }

//        viewModel.cookieLiveData.observe(this,{
//            Log.d("elementsScore", "searchScroe5: ${it}")
//
//        })


        //设置过度动画特效
        val doubleBounce: Sprite = DoubleBounce()
        binding.progressBar.setIndeterminateDrawable(doubleBounce)
    }


    override fun initEvent() {

//        binding.buttonScore.setOnClickListener {
////            scoreList = viewModel.scoreListLiveData.value!!
//            Log.d("elementsScore", "searchScroe6: ${scoreList[0].score}")
//            Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
//                .navigate(R.id.action_navigation_nwudoor_to_scroeFragment)
//        }

    }

    override fun getSubLayoutId() = R.layout.fragment_nwudoor
    override fun getSubVMClass() = NwudoorViewModel::class.java

    override fun initViewModel() {
        val IS_AUTO_LOGIN = AppPrefsUtils.getBoolean(BaseConstant.IS_AUTO_LOGIN_SCORE)
        val IS_REMEMBER_PASSWORD =
            AppPrefsUtils.getBoolean(BaseConstant.IS_REMEMBER_PASSWORD_SCORE)
        val NAME_SCORE = AppPrefsUtils.getString(BaseConstant.NAME_SCORE)
        val PASSWORD_SCORE = AppPrefsUtils.getString(BaseConstant.PASSWORD_SCORE)

        val ScoreSpBean =
            NetSpBean(NAME_SCORE!!, PASSWORD_SCORE!!, IS_REMEMBER_PASSWORD, IS_AUTO_LOGIN)

        viewModel =
            ViewModelProvider(this, NwudoorViewModelFactory(ScoreSpBean)).get(getSubVMClass())
    }


}