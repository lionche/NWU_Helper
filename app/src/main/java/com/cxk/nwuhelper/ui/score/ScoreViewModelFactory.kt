package com.cxk.nwuhelper.ui.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean

class ScoreViewModelFactory(private val netSpBean: NetSpBean) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ScoreViewModel(netSpBean) as T
    }
}