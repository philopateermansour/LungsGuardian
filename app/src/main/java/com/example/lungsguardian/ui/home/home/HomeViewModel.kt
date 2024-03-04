package com.example.lungsguardian.ui.home.home

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.MlResponseModel
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.repository.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {

    private val _modelValidate = MutableLiveData<String>()
    val modelValidate get() = _modelValidate
    private val _responseLiveData = MutableLiveData<Response<MlResponseModel>>()
    val responseLiveData get() = _responseLiveData

    fun sendImageToModel(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
              repo.sendImageToModel(image){
                  if (it?.code()==200){
                      _responseLiveData.postValue(it)
                  }
                  else{
                      _modelValidate.postValue(it?.message())

                  }
              }

        }catch (e:IOException){
            e.printStackTrace()
                _modelValidate.postValue(e.localizedMessage)
        }
        }
    }
}