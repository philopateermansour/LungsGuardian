package com.example.lungsguardian.ui.home.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeSharedViewModel:ViewModel() {
    private var _isProfileChanged =MutableLiveData<Boolean>(false)
    val isProfileChanged get() = _isProfileChanged
}