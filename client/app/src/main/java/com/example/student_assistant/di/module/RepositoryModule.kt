package com.example.student_assistant.di.module

import com.example.student_assistant.data.repository.ProjectRepository
import com.example.student_assistant.data.repository.UserRepository
import com.example.student_assistant.di.scope.AppScope
import com.example.student_assistant.domain.repository.IProjectRepository
import com.example.student_assistant.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    @AppScope
    abstract fun bindsProjectRepository(impl: ProjectRepository): IProjectRepository

    @Binds
    @AppScope
    abstract fun bindsUserRepository(impl: UserRepository): IUserRepository
}