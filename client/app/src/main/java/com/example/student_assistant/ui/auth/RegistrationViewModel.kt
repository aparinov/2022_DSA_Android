package com.example.student_assistant.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_assistant.domain.entity.RegistrationInfo
import com.example.student_assistant.domain.entity.VerificationInfo
import com.example.student_assistant.domain.repository.IUserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val repository: IUserRepository) : ViewModel() {

    private val _user = MutableLiveData<RegistrationInfo>()
    private val _code = MutableLiveData<String>()
    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered
    private val _isVerified = MutableLiveData<Boolean>()
    val isVerified: LiveData<Boolean> = _isVerified
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _message.postValue(throwable.message)
    }

    fun register(email: String, name: String, surname: String, password: String) {
        if (_isRegistered.value == true) {
            return
        }
        val info = RegistrationInfo(email, name, surname, password)
        _user.value = info
        viewModelScope.launch(exceptionHandler) {
            val result = repository.register(info)
            _isRegistered.value = result.isSuccess
            if (result.isFailure) {
                _message.postValue(result.exceptionOrNull()?.message)
            } else {
                _message.postValue("Verification code is sent")
            }
        }
    }

    fun verify(code: String) {
        if (_isRegistered.value == true) {
            _code.value = code
            val info = VerificationInfo(_user.value!!.email, code)
            viewModelScope.launch(exceptionHandler) {
                val result = repository.verify(info)
                if (result.isFailure) {
                    _message.postValue(result.exceptionOrNull()?.message)
                } else {
                    _isVerified.postValue(true)
                    _message.postValue("Verification is successful")
                }
            }
        }
    }
}