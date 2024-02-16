package com.example.lungsguardian.ui.home.profile

import androidx.lifecycle.ViewModel
import com.example.lungsguardian.utils.LOGGED_STATE
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.USER_EMAIL
import com.example.lungsguardian.utils.USER_NAME

class ProfileViewModel :ViewModel() {

    fun clearUserData(){
        MySharedPreferences.setInShared(USER_NAME,"")
        MySharedPreferences.setInShared(USER_EMAIL,"")
        MySharedPreferences.setInShared(LOGGED_STATE,"")
    }
}