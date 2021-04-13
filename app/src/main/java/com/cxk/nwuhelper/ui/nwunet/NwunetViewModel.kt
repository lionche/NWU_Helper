package com.cxk.nwuhelper.ui.nwunet


import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.cxk.nwuhelper.MyApplication.Companion.context
import com.cxk.nwuhelper.ui.nwunet.model.LoginBean
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean


class NwunetViewModel(netSpBean: NetSpBean) : ViewModel() {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //   检测网络

    val buttonState = MutableLiveData<String>()

    fun netCheck() {
//        获取 ConnectivityManager 的实例
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
//        使用此实例获取对应用当前默认网络的引用
//        val currentNetwork = connectivityManager.activeNetwork
//        通过对网络的引用，您的应用可以查询有关网络的信息


        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {

                val IpAddressByWifi = NetworkUtils.getIpAddressByWifi()

//                Log.e("test123", "getIpAddressByWifi,$IpAddressByWifi")


                if("10.1" in IpAddressByWifi ||"10.21" in IpAddressByWifi){
                    buttonState.postValue("wifi_available")
                }else{
                    buttonState.postValue("wifi_not_available")
                }
            }
        })


    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    登陆设备


    var name: String
    var password: String
    val autoLoginLiveData = MutableLiveData<Boolean>()
    val rmPasswordLiveData = MutableLiveData<Boolean>()

    init {
        name = netSpBean.name
        password = netSpBean.password
        autoLoginLiveData.value = netSpBean.autoLogin
        rmPasswordLiveData.value = netSpBean.rmPassword
    }

    var netAvailable = false

    val enable = MutableLiveData(false)

    fun judgeEnable() {
        enable.postValue(name.isNotEmpty() && password.isNotEmpty() && netAvailable)

        Log.d("password", "judgeEnable: ${enable.value}")
    }

    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        password = s.toString()
        judgeEnable()
    }

    fun onNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        name = s.toString()
        judgeEnable()
    }


    /**
     * 登陆nwunet校园网
     */


    fun loginNwunet() {

        val loginBean = LoginBean(name,password)
        loginDevices(loginBean)
        buttonState.value = "start_to_login"
    }

    private var loginDeviceLiveData = MutableLiveData<LoginBean>()

    val loginLiveData =
        Transformations.switchMap(loginDeviceLiveData) { LoginBean->
            Repository.loginDevices(LoginBean.name,LoginBean.password) }

    fun loginDevices(loginBean: LoginBean) {
        loginDeviceLiveData.value = loginBean
    }

    var loginResponseLiveData = MutableLiveData<String>()



}