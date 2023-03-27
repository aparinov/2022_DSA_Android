package com.example.student_assistant.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.student_assistant.domain.entity.Interest
import com.example.student_assistant.domain.entity.Status

class FilterViewModel : ViewModel() {

    private val _parameters = MutableLiveData<List<String>>()
    val parameters: LiveData<List<String>> = _parameters
    private val _isSwapped = MutableLiveData<Boolean>()
    val isSwapped: LiveData<Boolean> = _isSwapped

    // Todo: load parameters from sources
    private val statuses = listOf("Создан", "В процессе", "Завершен")

    init {
        _parameters.value = statuses
    }

    fun setIsSwapped(isSwapped: Boolean) {

    }
}