package com.cxk.nwuhelper.ui.nwudoor.score

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentScroeBinding
import com.cxk.nwuhelper.ui.base.BaseVMPFragment
import com.cxk.nwuhelper.ui.nwudoor.NwudoorViewModel
import com.cxk.nwuhelper.ui.nwudoor.NwudoorViewModelFactory
import com.cxk.nwuhelper.ui.nwudoor.score.adapter.ScoreAdapter
import com.cxk.nwuhelper.ui.nwudoor.score.model.ScoreData
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.AppPrefsUtils
import java.util.*

class ScroeFragment : BaseVMPFragment<FragmentScroeBinding, NwudoorViewModel>() {

    val scoreList = ArrayList<ScoreData>()

    override fun observerData() {
        Log.d("elementsScore", "searchScroe7: ${viewModel.cookieLiveData.value}")

    }


//    override fun startLoadData() {
//        Log.d("elementsScorevm", "searchScroe3: ${viewModel.cookieLiveData.value}")
//
//        val elementsScore = viewModel.cookieLiveData.value?.let { viewModel.searchScore(it) }
//        Log.d("elementsScorefragmet", "searchScroe: ${elementsScore}")
//
//        for (value in elementsScore!!) {
//            Log.d("website3detail", value.toString()+"---")
//
//            val subject: String = value.getElementsByTag("td")[3].text()
//            val score: String = value.getElementsByTag("td")[4].text()
//            Log.d("website3detail", "$subject,$score")
//
//            val scoreItem = ScoreData(subject,score)
//            scoreList.add(scoreItem)
//
//        }
//        setScoreItemRecycler(scoreList)
//    }


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