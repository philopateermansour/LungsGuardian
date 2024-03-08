package com.example.lungsguardian.ui.auth.image

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.UploadImageResponseModel
import com.example.lungsguardian.data.repository.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChangePictureViewModel @Inject constructor (private val repo:IRepo) :ViewModel() {

    private var _uploadImageLiveData = MutableLiveData<Response<UploadImageResponseModel>>()
    val uploadImageLiveData get() = _uploadImageLiveData

    private var _deleteImageLiveData = MutableLiveData<Response<String>>()
    val deleteImageLiveData get() = _deleteImageLiveData

    private var _errorLiveData = MutableLiveData<String>()
    val errorLiveData get() = _errorLiveData

    fun uploadProfileImage(imageFile: File){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.uploadProfileImage(imageFile){
                    if (it?.code()==200){
                        _uploadImageLiveData.postValue(it)
                    } else{
                        _errorLiveData.postValue(it?.message())
                        Log.e("philo", "uploadProfileImage: ${it?.body()} ", )
                    }
                }
            }catch (e: IOException){
                e.printStackTrace()
                _errorLiveData.postValue(e.localizedMessage)
            }
        }
    }
    fun deleteProfileImage(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                repo.deleteProfileImage {
                    if (it?.code()==200){
                        _deleteImageLiveData.postValue(it)
                    }else{
                        _errorLiveData.postValue(it?.message())
                    }
                }
            }catch (e: IOException){
                e.printStackTrace()
                _errorLiveData.postValue(e.localizedMessage)
            }
        }
    }
}