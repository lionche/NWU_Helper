package com.cxk.nwuhelper.ui.nwudoor.score

import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.MyApplication
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentShowpdfBinding
import com.cxk.nwuhelper.ui.base.BaseVMPFragment
import com.cxk.nwuhelper.ui.nwudoor.NwudoorViewModel
import com.cxk.nwuhelper.ui.nwudoor.NwudoorViewModelFactory
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.AppPrefsUtils
import com.zjy.pdfview.PdfView
import java.io.File


class showPdfFragment : BaseVMPFragment<FragmentShowpdfBinding, NwudoorViewModel>() {


    override fun observerData() {
        viewModel.buttonState.observe(
            this
        ) {
            when (it) {
                "load_pdf" -> {
                    loadPdf()
                }
            }
        }


        //    override fun observerData() {val document = PDDocument.load(File(MyApplication.context.filesDir, "temp.pdf"))
//        setScoreItemRecycler(NwudoorFragment.scoreList)
//        viewModel.scoreListLiveData.value = NwudoorFragment.scoreList
    }

    override fun initEvent() {
        loadPdf()

        binding.btnSearch.setOnClickListener{
            viewModel.Download2showPdf(binding.etName.text.toString(),NwudoorViewModel.cookies1!!)
        }


    }

    private fun loadPdf() {
        Log.d("showpdf", "loadPdf: 展示pdf")
        val file = File(MyApplication.context.filesDir, "temp.pdf")
        binding.pdfView.loadPdf(file.absolutePath)
    }


    override fun getSubLayoutId() = R.layout.fragment_showpdf

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