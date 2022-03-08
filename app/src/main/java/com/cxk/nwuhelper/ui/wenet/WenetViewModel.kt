package com.cxk.nwuhelper.ui.wenet

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.cxk.nwuhelper.MyApplication.Companion.context
import com.cxk.nwuhelper.ui.wenet.model.DeleteBean
import com.cxk.nwuhelper.ui.wenet.model.LoginPostBody
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean


class WenetViewModel(netSpBean: NetSpBean) : ViewModel() {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //   检测网络

    val buttonState = MutableLiveData<String>()
    lateinit var IpAddressByWifi: String


    fun netCheck() {
//        获取 ConnectivityManager 的实例
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
//        使用此实例获取对应用当前默认网络的引用
//        val currentNetwork = connectivityManager.activeNetwork
//        通过对网络的引用，您的应用可以查询有关网络的信息


        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                Log.e("test123", "现在的网络是$")
//            }

//            override fun onLost(network : Network) {
//                Log.e("test123", "刚刚断开网络,刚才连接的是 " + network)
//            }
//
//            override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
//                Log.e("test123", "The default network changed capabilities: " + networkCapabilities)
//            }


            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {


                IpAddressByWifi = NetworkUtils.getIpAddressByWifi()

                Log.e("test123", "getIpAddressByWifi,$IpAddressByWifi")

//                IpAddressByWifi.showToast(context)
                if ("10.1" in IpAddressByWifi) {
                    buttonState.postValue("wifi_available")
                } else {
                    buttonState.postValue("wifi_not_available")
                }

//                if ("192.168" in IpAddressByWifi) {
//                    "暂时不支持路由器".showToast(context)
//                    Log.d("test123", "onLinkPropertiesChanged:暂时不支持路由器 ")
//                    buttonState.postValue("wifi_not_available")
//                } else if ("10.1" in IpAddressByWifi) {
////                    "可以登陆".showToast(context)
//                    Log.d("test123", "onLinkPropertiesChanged:可以登陆2.4gip$IpAddressByWifi ")
//                    buttonState.postValue("wifi_available")
//                } else if ("10.8" in IpAddressByWifi) {
//                    "暂时不支持NWUNET".showToast(context)
//                    Log.d("test123", "onLinkPropertiesChanged:暂时不支持NWUNET ")
//                    buttonState.postValue("wifi_not_available")
//                } else {
////                    "流量".showToast(context)
////                    Log.d("test123", "onLinkPropertiesChanged:流量 ")
//                    buttonState.postValue("wifi_not_available")
//                }


            }
        })


    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    搜索设备


    var searchDeviceLiveData = MutableLiveData<String>()

//    var deviceList = ArrayList<SearchSessionsResponse.Sessions>()
    /**
     * 登陆设备检查
     */
    val deviceLiveData = Transformations.switchMap(searchDeviceLiveData) { authorization ->
        Repository.searchDevices(authorization)
    }

    //登陆时赋值
    val authorization = MutableLiveData<String>()
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
     * 登陆校园网
     */
    fun loginWenet() {

        //根据ip修改连接
        var url =
            "http://10.16.0.21:80/?usermac=XX:XX:XX:XX:XX:XX&userip=MYIP&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1"
//            "http://10.16.0.12:8081/?usermac=XX:XX:XX:XX:XX:XX&userip=MYIP&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1"
        url = url.replace("MYIP", IpAddressByWifi)

        val loginPostBody = LoginPostBody(
            redirectUrl = url,
            webAuthUser = name,
            webAuthPassword = password
        )

        Log.d("test123", "loginWenet:点击登录 $loginPostBody")

        loginDevices(loginPostBody)
        buttonState.value = "start_to_login"

    }

    private var loginDeviceLiveData = MutableLiveData<LoginPostBody>()

    val loginLiveData = Transformations.switchMap(loginDeviceLiveData) { loginPostBody ->
        Repository.loginDevices(loginPostBody)
    }

    fun loginDevices(loginPostBody: LoginPostBody) {
        loginDeviceLiveData.value = loginPostBody
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    删除设备


    private val deleteDeviceLiveData = MutableLiveData<DeleteBean>()


    val deleteLiveData = Transformations.switchMap(deleteDeviceLiveData) {
        Repository.deleteDevice(it.authorization, it.deviceId)
    }

    fun deleteDevice(authorization: String, deviceId: String) {
        val deleteBean = DeleteBean(authorization, deviceId)
        deleteDeviceLiveData.value = deleteBean
    }


}