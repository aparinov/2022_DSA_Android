package com.example.student_assistant.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    protected fun suspendableLaunch(action: (suspend () -> Unit)) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            action()
            _isLoading.postValue(false)
        }
    }
}