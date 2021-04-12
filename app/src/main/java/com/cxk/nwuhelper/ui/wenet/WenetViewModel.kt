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
import com.cxk.nwuhelper.utils.showToast


class WenetViewModel(val netSpBean: NetSpBean) : ViewModel() {

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

//                val linkAddresses = linkProperties.linkAddresses
//                val linkAddresses1 = linkAddresses.toString().indexOf("64, ")
//                val linkAddresses2 = linkAddresses.toString().lastIndexOf('/')
//
//                ipAddress = linkAddresses.toString()
//                    .substring(linkAddresses1 + 4, linkAddresses2)
//
//                val serverAddressLocation1 = linkProperties.toString().indexOf("ServerAddress")
//                val serverAddressLocation2 = linkProperties.toString().indexOf("TcpBufferSizes")
//                val serverAddress = linkProperties.toString()
//                    .substring(serverAddressLocation1 + 16, serverAddressLocation2)
//                serverAddress.showToast(context)

                IpAddressByWifi = NetworkUtils.getIpAddressByWifi()
                val ServerAddressByWifi = NetworkUtils.getServerAddressByWifi()
                val isWifiAvailable = NetworkUtils.isWifiAvailable()
                ("ip:${IpAddressByWifi},server:$ServerAddressByWifi").showToast(context)

                Log.e("test123", "getIpAddressByWifi,$IpAddressByWifi")
                Log.e("test123", "ServerAddressByWifi,$ServerAddressByWifi")
                Log.e("test123", "isWifiAvailable,$isWifiAvailable")


                if (!isWifiAvailable) {
//                    "请打开Wi-Fi".showToast(context)
                    buttonState.postValue("wifi_not_available")
                } else if ("192.168" in ServerAddressByWifi) {
//                    "暂时不支持路由器".showToast(context)
                    buttonState.postValue("wifi_not_available")
                } else if ("254.254" in ServerAddressByWifi) {
//                    "可以登陆".showToast(context)
                    buttonState.postValue("wifi_available")
                } else if ("172.18.6.6" in ServerAddressByWifi) {
//                    "暂时不支持NWUNET".showToast(context)
                    buttonState.postValue("wifi_not_available")
                } else {
//                    "流量".showToast(context)
                    buttonState.postValue("wifi_not_available")
                }


//                when (serverAddress.split('.')[0]) {
//                    "10" -> {
//                        buttonState.postValue("wifi_available")
////                        Toast.makeText(context, "ip地址:$ipAddress,服务器地址:$serverAddress", Toast.LENGTH_SHORT).show()
////                        Toast.makeText(context, "欢迎登陆", Toast.LENGTH_SHORT).show()
//                    }
//                    "172" -> {
//                        "暂时不支持NWUNET".showToast(context)
//                        buttonState.postValue("wifi_not_available")
//
//                    }
//                    "192" -> {
//                        "暂时不支持路由器".showToast(context)
//                        buttonState.postValue("wifi_not_available")
//                    }
//                    else -> {
////                        "请连接校园网".showToast(context)
//                        buttonState.postValue("wifi_not_available")
//                    }
//
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
            "http://10.16.0.12:8081/?usermac=XX:XX:XX:XX:XX:XX&userip=MYIP&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1"
        url = url.replace("MYIP", IpAddressByWifi)

        val loginPostBody = LoginPostBody(
            redirectUrl = url,
            webAuthUser = name,
            webAuthPassword = password
        )
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