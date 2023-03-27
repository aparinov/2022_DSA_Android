package com.example.student_assistant.ui.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.student_assistant.R
import com.example.student_assistant.domain.entity.CreateProjectInfo
import com.example.student_assistant.domain.entity.Parameter
import com.example.student_assistant.domain.entity.Project
import com.example.student_assistant.domain.repository.IProjectRepository
import com.example.student_assistant.ui.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProjectViewModel @Inject constructor(
    private val repository: IProjectRepository,
) : BaseViewModel() {

    private val _project = MutableLiveData<Project?>()
    val project: LiveData<Project?> = _project
    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id
    private val _updated = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> = _updated
    private val _isAuthor = MutableLiveData<Boolean>()
    val isAuthor: LiveData<Boolean> = _isAuthor

    val list = listOf("Статус проекта", "Статус набора", "Тэги")
    val statuses = listOf("Не начат", "Начат", "Завершен")
    val pages = listOf(R.id.param_project_status_rb, R.id.param_rec_status_rb, R.id.param_tags_rb)
    private var tagList = MutableLiveData<List<String>>()

    private val _parameters = MutableLiveData<List<Parameter>>()
    val parameters: LiveData<List<Parameter>> = _parameters
    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> = _page
    private val _parameter = MediatorLiveData<Parameter>()
    val parameter: LiveData<Parameter> = _parameter

    init {
        loadTagList()
        page.observeForever {
            _parameter.value = if (it == R.id.param_project_status_rb) {
                _parameters.value?.get(0)
            } else if (it == R.id.param_rec_status_rb) {
                _parameters.value?.get(1)
            } else if (it == R.id.param_tags_rb) {
                _parameters.value?.get(2)
            } else {
                throw IllegalStateException()
            }
        }
    }

    private fun loadTagList() {
        suspendableLaunch {
            val result = repository.getTags()
            if (result.isSuccess) {
                tagList.postValue(result.getOrNull())
                _parameters.postValue(
                    list.mapIndexed { index, name ->
                        if (index < 2) {
                            Parameter(name, statuses, mutableSetOf(0), pages[index])
                        } else {
                            Parameter(name, result.getOrNull()!!, mutableSetOf(), pages[index])
                        }
                    }
                )
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }

    fun setId(id: Int = _id.value!!) {
        _id.value = id
        if (id != -1) {
            viewModelScope.launch {
                val result = repository.getProject(id)
                if (result.isSuccess) {
                    _project.postValue(result.getOrNull())
                    val result1 = repository.isUserTheAuthor(result.getOrNull()!!)
                    _isAuthor.postValue(result1.getOrNull())
                } else {
                    _message.postValue(result.exceptionOrNull()?.message)
                }
            }
        } else {
            _project.postValue(null)
        }
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
    
    fun setPage(page: Int) {
        _page.value = page
    }

    fun reset() {
        _parameters.value = _parameters.value?.map {
            it.copy(chosen = mutableSetOf())
        }
    }

    fun setProject(
        title: String,
        description: String,
        maxNumberOfStudents: Int,
        applicationsDeadline: String,
        plannedStartOfWork: String,
        plannedFinishOfWork: String,
    ) {
        val values = _parameters.value?.map { parameter ->
            parameter.chosen.map { index ->
                parameter.values[index]
            }
        } ?: throw IllegalStateException()
        if (id.value != null
            && id.value != -1) {
            update(
                id.value!!,
                title,
                description,
                values[1][0],
                values[0][0],
                applicationsDeadline,
                plannedStartOfWork,
                plannedFinishOfWork,
                values[2],
            )
        } else {
            add(
                title,
                description,
                maxNumberOfStudents,
                values[1][0],
                values[0][0],
                applicationsDeadline,
                plannedStartOfWork,
                plannedFinishOfWork,
                values[2],
            )
        }
    }

    private fun update(
        id: Int,
        title: String,
        description: String,
        recruitingStatus: String,
        projectStatus: String,
        applicationsDeadline: String,
        plannedStartOfWork: String,
        plannedFinishOfWork: String,
        tags: List<String>,
    ) {
        val project = _project.value ?: return
        viewModelScope.launch {
            val result = repository.updateProject(
                Project(
                    id,
                    project.authorEmail,
                    project.author,
                    title,
                    description,
                    -1,
                    -1,
                    recruitingStatus,
                    projectStatus,
                    applicationsDeadline,
                    plannedStartOfWork,
                    plannedFinishOfWork,
                    tags
                )
            )
            if (result.isSuccess) {
                _updated.postValue(true)
                _message.postValue("Project updated successfully")
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }
    private fun add(
        title: String,
        description: String,
        maxNumberOfStudents: Int,
        recruitingStatus: String,
        projectStatus: String,
        applicationsDeadline: String,
        plannedStartOfWork: String,
        plannedFinishOfWork: String,
        tags: List<String>,
    ) {
        viewModelScope.launch {
            val result = repository.addProject(
                CreateProjectInfo(
                    title,
                    description,
                    maxNumberOfStudents,
                    recruitingStatus,
                    projectStatus,
                    applicationsDeadline,
                    plannedStartOfWork,
                    plannedFinishOfWork,
                    tags,
                )
            )
            if (result.isSuccess) {
                _updated.postValue(true)
                _message.postValue("Project created successfully")
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }

    fun deleteProject() {
        viewModelScope.launch {
            val result = repository.deleteProject(_id.value!!)
            if (result.isSuccess) {
                _message.postValue("Project deleted successfully")
                _updated.postValue(true)
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }

    fun join() {
        if (id.value == null) {
            return
        }
        suspendableLaunch {
            val result = repository.joinProject(id.value!!)
            if (result.isSuccess) {
                _message.postValue("You joined the project successfully")
            } else {
                _message.postValue(result.exceptionOrNull()?.message)
            }
        }
    }
}