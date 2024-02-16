package com.example.lungsguardian.utils

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {

    private var myAppContext :Context ?= null

    private fun mySharedPreferences(){}

    fun init(context: Context){
        myAppContext = context
    }
    private fun getMySharedPreferences() : SharedPreferences{
        return myAppContext!!.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
    }
    fun setInShared(sharedStore : String,value: String){
        getMySharedPreferences().edit().putString(sharedStore,value).apply()
    }
    fun getFromShared(sharedStore: String) : String{
        return getMySharedPreferences().getString(sharedStore,"")!!
    }
}