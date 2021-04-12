package com.cxk.nwuhelper


object BaseConstant {
    const val TABLE_PREFS = "nwu_helper"
    //西北大学统一门户认证
    const val NWU_LOGIN_URL = "http://authserver.nwu.edu.cn/authserver/login"
    const val NWU_LOGIN_URL_BASE = "http://authserver.nwu.edu.cn/authserver/"
    //登陆统一门户后,查询成绩
    const val SCORE_URL = "http://yjsxt.nwu.edu.cn/procedure/student/modifymidexam.htm"

    //WENET
    const val WENET_URL = "http://10.16.0.12:8081/portal/api/v2/"
    const val IS_AUTO_LOGIN_WENET = "IS_AUTO_LOGIN_WENET"
    const val IS_REMEMBER_PASSWORD_WENET = "IS_REMEMBER_PASSWORD_WENET"
    const val NAME_WENET = "NAME_WENET"
    const val PASSWORD_WENET = "PASSWORD_WENET"

    //NWUNET
    const val NWUNET_URL = "http://10.0.1.242/"
    const val IS_AUTO_LOGIN_NWUNET = "IS_AUTO_LOGIN_NWUNET"
    const val IS_REMEMBER_PASSWORD_NWUNET = "IS_REMEMBER_PASSWORD_NWUNET"
    const val NAME_NWUNET = "NAME_NWUNET"
    const val PASSWORD_NWUNET = "PASSWORD_NWUNET"

}