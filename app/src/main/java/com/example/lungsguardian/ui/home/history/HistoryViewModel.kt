package com.example.lungsguardian.ui.home.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.HistoryModel
import com.example.lungsguardian.data.repository.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel  @Inject constructor(private val repo:IRepo)  : ViewModel() {


    private val _historyValidate = MutableLiveData<String>()
    val historyValidate get() = _historyValidate
    private val _responseLiveData = MutableLiveData<Response<HistoryModel>>()
    val responseLiveData get() = _responseLiveData

    private val _deleteLiveData = MutableLiveData<Response<String>>()
    val deleteLiveData get() = _deleteLiveData


    fun showHistory(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                repo.showHistory {
                    if (it?.code()==200){
                    _responseLiveData.postValue(it)
                }
                    else{
                        _historyValidate.postValue(it?.message())
                    }
                }

            }catch (e:IOException){
                e.printStackTrace()
                _historyValidate.postValue(e.localizedMessage)
            }
        }
    }
    fun deleteReport(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteReport(id){
                    if (it?.code()==200){
                        _deleteLiveData.postValue(it)
                    }else{
                        _historyValidate.postValue(it?.message())
                    }
                }
            }catch (e:IOException){
                e.printStackTrace()
                _historyValidate.postValue(e.localizedMessage)
            }
        }
    }

}