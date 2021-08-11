package com.cxk.nwuhelper.ui.score

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cxk.nwuhelper.BaseConstant.NWU_LOGIN_URL
import com.cxk.nwuhelper.BaseConstant.SCORE_URL
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.encrypt
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ScoreViewModel(netSpBean: NetSpBean) : ViewModel() {

    var name: String
    var password: String
    val autoLoginLiveData = MutableLiveData<Boolean>()
    val rmPasswordLiveData = MutableLiveData<Boolean>()

    val enable = MutableLiveData(false)
    val resultMap = MutableLiveData<MutableMap<String, String>>()

    init {
        name = netSpBean.name
        password = netSpBean.password
        autoLoginLiveData.value = netSpBean.autoLogin
        rmPasswordLiveData.value = netSpBean.rmPassword
    }


    fun judgeEnable() {
        enable.value = name.isNotEmpty() && password.isNotEmpty()
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

    fun visitWebsiteNewThread() {
        object : Thread() {
            override fun run() {
                visitWebsite()
            }
        }.start()
        buttonState.postValue("start_to_login")
    }

    fun visitWebsite() {
        Log.d("chengji", "开始查询")

        val connect: Connection = Jsoup.connect(NWU_LOGIN_URL)
//        connect.header(USER_AGENT, USER_AGENT_VALUE) // 配置模拟浏览器
        val response: Connection.Response = connect.execute()
        val cookies: Map<String, String> = response.cookies()
        //        System.out.println("cookies:" + cookies);
        val document = Jsoup.parse(response.body())
        Log.d("website1", document.toString())
        val stringStringMap: Map<String, String> = extractInfo(document)
        loginWebsite(cookies, stringStringMap)
    }

    private fun loginWebsite(cookies: Map<String, String>, stringStringMap: Map<String, String>) {
        val lt = stringStringMap["lt"]
        val execution = stringStringMap["execution"]
        val passwordAfterEncrypt = stringStringMap["password"]
        val connect = Jsoup.connect(NWU_LOGIN_URL).cookies(cookies).followRedirects(false)
        connect.data("username", name)
        connect.data("password", passwordAfterEncrypt)
        connect.data("lt", lt)
        connect.data("dllt", "userNamePasswordLogin")
        connect.data("execution", execution)
        connect.data("_eventId", "submit")
        connect.data("rmShown", "1")
        connect.method(Connection.Method.POST)
        val response = connect.execute()
        val cookies1 = response.cookies()
        val document = Jsoup.parse(response.body()).toString()
        Log.d("website2", document)

        //登陆成功
        if ("This document you requested has moved temporarily" in document){
            buttonState.postValue("login_success")
            searchScroe(cookies1)
        }
        else {
            buttonState.postValue("wrong_password")

        }

    }


    private fun extractInfo(document: Document): Map<String, String> {
        val aesKey: String =
                document.getElementsByTag("head").first().getElementsByTag("script").eq(1).toString()
                        .split("\"")[5]
        //        System.out.println("aes key:" + aes_key);
        val lt: String = document.getElementsByTag("input").eq(4).`val`()
        //        System.out.println("lt:" + lt);
        val execution: String = document.getElementsByTag("input").eq(6).`val`()
        //        System.out.println("execution:" + execution);
        val passwordAfterEncrypt = encrypt.Encrypt(password, aesKey)
        //        System.out.println("passwordAfterEncrypt:" + passwordAfterEncrypt);
        val map: MutableMap<String, String> = HashMap()
        map["password"] = passwordAfterEncrypt
        map["lt"] = lt
        map["execution"] = execution
        return map
    }

    private fun searchScroe(cookies1: Map<String, String>) {
        Log.d("website3", "访问website3")
        val connect = Jsoup.connect(SCORE_URL).cookies(cookies1).followRedirects(true).timeout(10000)
        connect.method(Connection.Method.GET)
        val response = connect.execute()
        val document = Jsoup.parse(response.body())
        Log.d("website3", document.toString())
        giveScore(document)
    }



    private fun giveScore(document: Document) {
        val elements: Elements = document.getElementsByTag("tbody").first().getElementsByTag("tr")
//        Log.d("website3detail", elements.toString())
        val scoreMap: MutableMap<String, String> = HashMap()


        //        System.out.println(elements.first().getElementsByTag("td").get(0).text());
        for (value in elements) {
            Log.d("website3detail", value.toString()+"---")

            val subject: String = value.getElementsByTag("td")[3].text()
            val score: String = value.getElementsByTag("td")[4].text()
            Log.d("website3detail", "$subject,$score")

//            scoreMap.put(subject, score)
            scoreMap[subject] = score
        }

        resultMap.postValue(scoreMap)


    }

    val buttonState = MutableLiveData<String>()



}
