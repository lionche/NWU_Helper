package com.cxk.nwuhelper.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.che.haccp.ui.base.BaseVMFragment
import com.cxk.nwuhelper.MyApplication
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.ScoreActivity
import com.cxk.nwuhelper.databinding.FragmentDashboardBinding
import com.cxk.nwuhelper.utils.SerializableMap


class DashboardFragment : BaseVMFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override fun observerData() {
        binding.model = viewModel
        binding.lifecycleOwner = this


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


    }










    override fun getSubLayoutId() = R.layout.fragment_dashboard
    override fun getSubVMClass() = DashboardViewModel::class.java

}