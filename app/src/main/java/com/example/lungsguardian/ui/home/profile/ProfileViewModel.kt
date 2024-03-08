package com.example.lungsguardian.ui.home.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.CurrentUserDataModel
import com.example.lungsguardian.data.model.UploadImageResponseModel
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.repository.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {

    private var _profileLiveData = MutableLiveData<Response<CurrentUserDataModel>>()
     val profileLiveData get() = _profileLiveData

    private var _errorLiveData = MutableLiveData<String>()
     val errorLiveData get() = _errorLiveData



    fun showProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.showProfile {
                    _profileLiveData.postValue(it)
                }
            } catch (e: IOException) {
                _errorLiveData.postValue(e.localizedMessage)
            }
        }
    }

}