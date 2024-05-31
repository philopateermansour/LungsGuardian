package com.example.lungsguardian.ui.auth.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.utils.LOGGED_IN
import com.example.lungsguardian.utils.LOGGED_STATE
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.VALIDATE_CODE_NULL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(private val repo :IRepo) :ViewModel(){


    private var _confirmEmailLiveData = MutableLiveData<Response<String>>()
    val confirmEmailLiveData get() = _confirmEmailLiveData
    private var _errorLiveData = MutableLiveData<String>()
    val errorLiveData get() = _errorLiveData


    fun validate(email:String,code:String){
        if (code.isEmpty()){
            _errorLiveData.value= VALIDATE_CODE_NULL
        }
        else{
            confirmEmail(email, code)
        }
    }
    private fun confirmEmail(email:String,code:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                 repo.confirmEmail(email,code){
                    if (it?.code()==200){
                        _confirmEmailLiveData.postValue(it)
                        cacheLogged()
                    }
                     else{
                         _errorLiveData.postValue(it?.message())
                     }
                }
            }catch (e:IOException){
                e.printStackTrace()
                _errorLiveData.postValue(e.localizedMessage)
            }
        }
    }

    private fun cacheLogged() {
        MySharedPreferences.setInShared(LOGGED_STATE, LOGGED_IN)
    }
}