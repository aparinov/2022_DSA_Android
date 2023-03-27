package com.example.student_assistant.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.student_assistant.domain.repository.IProjectRepository
import com.example.student_assistant.domain.repository.IUserRepository
import com.example.student_assistant.ui.filter.FilterViewModel
import com.example.student_assistant.ui.auth.AuthViewModel
import com.example.student_assistant.ui.auth.RegistrationViewModel
import com.example.student_assistant.ui.main.MainViewModel
import com.example.student_assistant.ui.profile.ProfileViewModel
import com.example.student_assistant.ui.project.ProjectViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val projectRepository: IProjectRepository,
    private val userRepository: IUserRepository,
    ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java))
            MainViewModel(projectRepository, userRepository) as T
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            ProfileViewModel(userRepository, projectRepository) as T
        else if (modelClass.isAssignableFrom(ProjectViewModel::class.java))
            ProjectViewModel(projectRepository) as T
        else if (modelClass.isAssignableFrom(FilterViewModel::class.java))
            FilterViewModel() as T
        else if (modelClass.isAssignableFrom(RegistrationViewModel::class.java))
            RegistrationViewModel(userRepository) as T
        else if (modelClass.isAssignableFrom(AuthViewModel::class.java))
            AuthViewModel(userRepository) as T
        else
            throw IllegalArgumentException("ViewModel $modelClass Not Found")
    }
}