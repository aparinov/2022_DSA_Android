package com.example.student_assistant.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.student_assistant.R
import com.example.student_assistant.domain.entity.Card
import com.example.student_assistant.domain.entity.Parameter
import com.example.student_assistant.domain.repository.IProjectRepository
import com.example.student_assistant.domain.repository.IUserRepository
import com.example.student_assistant.ui.BaseViewModel

class MainViewModel(
    private val projectRepository: IProjectRepository,
    private val userRepository: IUserRepository,
) : BaseViewModel() {

    private val _isAuthorized = MutableLiveData<Boolean>()
    val isAuthorized: LiveData<Boolean> = _isAuthorized
    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching
    private val _cards = MutableLiveData<List<Card>>(emptyList())
    val cards: LiveData<List<Card>> = _cards
    private val _page = MutableLiveData(R.id.rb_all)
    val page: LiveData<Int> = _page

    private val _pPage = MutableLiveData(R.id.param_project_status_rb)
    val pPage: LiveData<Int> = _pPage
    private val names = listOf("Статус проекта", "Статус набора")
    private val statuses = listOf("Не начат", "Начат", "Завершен")
    private val pages = listOf(R.id.param_project_status_rb, R.id.param_rec_status_rb)
    private val _parameters = MutableLiveData<List<Parameter>>()
    val parameters: LiveData<List<Parameter>> = _parameters

    init {
        _parameters.value = names.mapIndexed {index, name ->
            Parameter(name, statuses, mutableSetOf(0), pages[index])
        }
    }

    fun setIsSearching(value: Boolean) {
        _isSearching.value = value
    }

    fun reset() {
        _parameters.value = _parameters.value?.map {
            it.copy(chosen = mutableSetOf(0))
        }
    }

    fun checkIfAuthorized() {
        suspendableLaunch {
            val result = userRepository.getCachedUser()
            _isAuthorized.postValue(result.isSuccess)
        }
    }

    fun setPPage(value: Int) {
        _pPage.value = value
    }

    fun setChosenItem(parameter: Parameter) {
        _parameters.value = _parameters.value?.map {
            if (it.name == parameter.name) {
                parameter
            } else {
                it
            }
        }
    }

    fun setPage(value: Int = _page.value!!) {
        _page.value = value
        suspendableLaunch {
            val result = if (value == R.id.rb_all) {
                projectRepository.getProjects()
            } else if (value == R.id.rb_rec) {
                projectRepository.getRecommendedProjects()
            } else if (value == R.id.rb_apply) {
                projectRepository.getProjects()
            } else {
                Result.failure(IllegalStateException())
            }
            if (result.isSuccess) {
                val list = if (value == R.id.rb_all) {
                    result.getOrNull()?.filter { it.status.isBlank() || it.status.contains(",") }
                } else if (value == R.id.rb_apply) {
                    result.getOrNull()?.filter { it.status.contains("Заявка") }
                } else if (value == R.id.rb_rec) {
                    result.getOrNull()
                } else {
                    listOf()
                }
                _cards.postValue(list!!)
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }

    fun setQuery(query: String) {
        val parameters = _parameters.value ?: return
        val statuses = parameters.map { p ->
            p.chosen.map { i ->
                p.values[i]
            }
        }
        suspendableLaunch {
            val result = projectRepository.searchProject(query, statuses[0][0], statuses[1][0])
            if (result.isSuccess) {
                _cards.postValue(result.getOrNull())
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }
}