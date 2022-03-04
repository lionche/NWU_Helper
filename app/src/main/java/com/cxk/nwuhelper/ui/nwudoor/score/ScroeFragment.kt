package com.cxk.nwuhelper.ui.nwudoor.score

import android.app.Activity
import android.app.Instrumentation
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentScroeBinding
import com.cxk.nwuhelper.ui.base.BaseVMPFragment
import com.cxk.nwuhelper.ui.nwudoor.NwudoorFragment
import com.cxk.nwuhelper.ui.nwudoor.NwudoorViewModel
import com.cxk.nwuhelper.ui.nwudoor.NwudoorViewModelFactory
import com.cxk.nwuhelper.ui.nwudoor.score.adapter.ScoreAdapter
import com.cxk.nwuhelper.ui.nwudoor.score.model.ScoreData
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.AppPrefsUtils



class ScroeFragment : BaseVMPFragment<FragmentScroeBinding, NwudoorViewModel>() {


    override fun observerData() {


//        val extractPdf = ExtractPdf()
//        extractPdf.extractPdf()


        //    override fun observerData() {val document = PDDocument.load(File(MyApplication.context.filesDir, "temp.pdf"))
        setScoreItemRecycler(NwudoorFragment.scoreList)
//        viewModel.scoreListLiveData.value = NwudoorFragment.scoreList
    }

    override fun initEvent() {
//        binding.btnBackToDoor.setOnClickListener {
//            object : Thread() {
//                override fun run() {
//                    try {
//                        val inst = Instrumentation()
//                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }.start()
//        }
        binding.btnBackToDoor.setOnClickListener {
            Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
                .navigate(R.id.action_scroeFragment_to_showPdfFragment)

        }
    }

    private fun setScoreItemRecycler(scoresList: List<ScoreData>) {
        binding.productRecycler.adapter = ScoreAdapter(requireContext(), scoresList)
        binding.productRecycler.setHasFixedSize(true)
    }


    override fun getSubLayoutId() = R.layout.fragment_scroe

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




    override fun getSubVMClass() = NwudoorViewModel::class.java


}