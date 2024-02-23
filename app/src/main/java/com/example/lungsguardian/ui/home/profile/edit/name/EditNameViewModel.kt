package com.example.lungsguardian.ui.home.profile.edit.name

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.Name
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.USER_EMAIL
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_INVALID
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_NULL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EditNameViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {

    private var _validateLiveData = MutableLiveData<String>()
    val validateLiveData get() = _validateLiveData

    private var _editNameResponseLiveData = MutableLiveData<Response<String>>()
    val editNameResponseLiveData get() = _editNameResponseLiveData
    fun validate(name: String) {
        if (name.isEmpty()) {
            _validateLiveData.value = VALIDATE_FULL_NAME_NULL
        } else if (!isFullNameValid(name)) {
            _validateLiveData.value = VALIDATE_FULL_NAME_INVALID
        } else {
            editName(name)
        }
    }

    private fun editName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.editName(name) {
                    if (it?.code() == 200) {
                        _editNameResponseLiveData.postValue(it)
                        Log.d("TAG", "editName: ")
                    } else {
                        _validateLiveData.postValue(it?.message())
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                _validateLiveData.postValue(e.localizedMessage)
            }
        }
    }

    private fun isFullNameValid(fullName: String): Boolean {
        return fullName.contains(" ")
    }
}