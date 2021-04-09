package com.cxk.nwuhelper.ui.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseVMPFragment<T : ViewDataBinding, VM : ViewModel> : BaseViewFragment<T>() {

    protected lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //创建viewModel
        initViewModel()
        //观察数据变化--->更新UI
        observerData()
        //设置相关事件
        initEvent()
        //开始加载数据
        startLoadData()
    }

    open fun startLoadData(){

    }

    /**
     * 设置相关事件
     */
    open fun initEvent(){

    }

    /**
     * 观察数据变化--->更新UI
     */
    open fun observerData(){

    }

    /**
     * 创建viewModel
     */
    abstract fun initViewModel()
    abstract fun getSubVMClass(): Class<VM>


}