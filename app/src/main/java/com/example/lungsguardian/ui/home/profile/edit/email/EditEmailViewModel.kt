package com.example.lungsguardian.ui.home.profile.edit.email

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.utils.EMAIL_REGISTERED
import com.example.lungsguardian.utils.FALSE
import com.example.lungsguardian.utils.TRUE
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EditEmailViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {

    private var _editEmailValidateLiveData = MutableLiveData<String>()
    val editEmailValidateLiveData get() = _editEmailValidateLiveData

    private var _editEmailResponseLiveData = MutableLiveData<Response<String>>()
    val editEmailResponseLiveData get() = _editEmailResponseLiveData


    suspend fun validation(email: String) {
        if (email.isEmpty()) {
            _editEmailValidateLiveData.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)) {
            _editEmailValidateLiveData.value = VALIDATE_EMAIL_INVALID
        } else {
            checkIfEmailExists(email)
        }
    }


     fun editEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.editEmail(email) {
                    if (it?.code() == 200) {
                        _editEmailResponseLiveData.postValue(it)
                    } else {
                        _editEmailValidateLiveData.postValue(it?.message())
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                _editEmailValidateLiveData.postValue(e.localizedMessage)
            }
        }
    }
    private fun checkIfEmailExists(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.checkIfEmailExists(email) {
                    if (it!!.equals(TRUE)){
                        _editEmailValidateLiveData.postValue(it)}
                    else if (it!!.equals(FALSE)){
                        _editEmailValidateLiveData.postValue(it)
                    }}
            }catch (e:IOException){
                e.printStackTrace()
                _editEmailValidateLiveData.postValue(e.localizedMessage)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}