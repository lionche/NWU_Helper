package com.cxk.nwuhelper.ui.nwunet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.ui.wenet.model.WenetSpBean

class NwunetViewModelFactory(private val wenetSpBean: WenetSpBean) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NwunetViewModel(wenetSpBean) as T
    }
}