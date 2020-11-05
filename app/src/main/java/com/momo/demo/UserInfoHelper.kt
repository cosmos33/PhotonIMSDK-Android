package com.momo.demo

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object UserInfoHelper {
    data class userNameAndPwd(var name:String,var pwd:String)
    var userNameAndPwdList:MutableSet<userNameAndPwd> = mutableSetOf()

    fun getPassWd(userName:String) = userNameAndPwdList.filter { it.name == userName }?.get(0)?.pwd
    fun getUserNameList() = userNameAndPwdList.map { it.name }
}