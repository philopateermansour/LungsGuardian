package com.example.lungsguardian.ui.report.showLoading

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.PredictionModel
import com.example.lungsguardian.data.repository.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {

    private val _modelValidate = MutableLiveData<String>()
    val modelValidate get() = _modelValidate
    private val _responseLiveData = MutableLiveData<Response<PredictionModel>>()
    val responseLiveData get() = _responseLiveData

    fun sendImageToModel(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
              repo.sendImageToModel(file){
                  if (it?.code()==200){
                          _responseLiveData.postValue(it)
                      Log.e("TAG", "done: ${it.body()} " )
                  }
                  else{
                      _modelValidate.postValue(it?.message())
                      Log.e("TAG", "failed: " )
                  }
              }
        }catch (e:IOException){
            e.printStackTrace()
                _modelValidate.postValue(e.localizedMessage)
                Log.e("TAG", "exception: ${e.message.toString()} ", )
        }
        }
    }
}