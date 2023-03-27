package com.example.student_assistant.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_assistant.domain.entity.LoginInfo
import com.example.student_assistant.domain.repository.IUserRepository
import com.example.student_assistant.ui.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val userRepository: IUserRepository,
) : BaseViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun checkIfAuthorized() {
        suspendableLaunch {
            val result = userRepository.getCachedUser()
            if (result.isSuccess) {
                _isLoggedIn.postValue(true)
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _message.postValue("Invalid data")
        }
        suspendableLaunch {
            val result = userRepository.login(LoginInfo(email, password))
            if (result.isSuccess) {
                _isLoggedIn.postValue(true)
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }
}