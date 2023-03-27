package com.example.student_assistant.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_assistant.R
import com.example.student_assistant.domain.entity.Parameter
import com.example.student_assistant.domain.entity.UserInfo
import com.example.student_assistant.domain.repository.IProjectRepository
import com.example.student_assistant.domain.repository.IUserRepository
import com.example.student_assistant.ui.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val projectRepository: IProjectRepository,
) : BaseViewModel() {

    private val _user = MutableLiveData<UserInfo>()
    val user: LiveData<UserInfo> = _user
    private val _isUpdated = MutableLiveData<Boolean>()
    val isUpdated: LiveData<Boolean> = _isUpdated

    val list = listOf("Тэги")
    val pages = listOf(R.id.param_tags_rb)

    private val _parameters = MutableLiveData<List<Parameter>>()
    val parameters: LiveData<List<Parameter>> = _parameters
    private val _page = MutableLiveData(R.id.param_tags_rb)
    val page: LiveData<Int> = _page
    private val _parameter = MediatorLiveData<Parameter>()
    val parameter: LiveData<Parameter> = _parameter

    fun setChosenItem(parameter: Parameter) {
        _parameters.value = _parameters.value?.map {
            if (it.name == parameter.name) {
                parameter
            } else {
                it
            }
        }
    }

    fun reset() {
        _parameters.value = _parameters.value?.map {
            it.copy(chosen = mutableSetOf())
        }
    }

    private fun loadTagList(user: UserInfo?) {
        if (user == null) return
        suspendableLaunch {
            val result = projectRepository.getTags()
            if (result.isSuccess) {
                val tagList = result.getOrNull()!!
                val indexes = List(tagList.size) { i -> i }.filter { user.tags.contains(tagList[it]) }.toMutableSet()
                _parameters.postValue(listOf(Parameter(
                    list[0],
                    tagList,
                    indexes,
                    pages[0])))
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }

    fun update(name: String, surname: String, bio: String) {
        val parameter = parameters.value?.get(0) ?: return
        val tags = parameter.chosen.map {
            parameter.values[it]
        }
        suspendableLaunch {
            val result = userRepository.updateUser(name, surname, bio, tags)
            if (result.isSuccess) {
                _message.postValue("Your profile data is changed successfully")
                _isUpdated.postValue(true)
                val oldUser = _user.value
                if (oldUser != null) {
                    _user.postValue(UserInfo(oldUser.isStudent, name + " " + surname, bio, oldUser.contacts, tags))
                }
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }

    fun loadUser() {
        suspendableLaunch {
            val result = userRepository.getUser()
            if (result.isSuccess) {
                _user.postValue(result.getOrNull())
                loadTagList(result.getOrNull())
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }

    fun logout() {
        suspendableLaunch {
            userRepository.logout()
        }
    }
}