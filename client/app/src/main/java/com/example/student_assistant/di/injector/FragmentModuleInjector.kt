package com.example.student_assistant.di.injector

import com.example.student_assistant.di.scope.FragmentScope
import com.example.student_assistant.ui.filter.FilterFragment
import com.example.student_assistant.ui.auth.AuthFragment
import com.example.student_assistant.ui.auth.RegistrationFragment
import com.example.student_assistant.ui.main.MainFragment
import com.example.student_assistant.ui.main.MainParametersFragment
import com.example.student_assistant.ui.project.ProjectParameterFragment
import com.example.student_assistant.ui.profile.ProfileEditFragment
import com.example.student_assistant.ui.profile.ProfileFragment
import com.example.student_assistant.ui.profile.ProfileParameterFragment
import com.example.student_assistant.ui.project.ProjectDetailFragment
import com.example.student_assistant.ui.project.ProjectEditFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModuleInjector {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeProjectParametersFragment(): ProjectParameterFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeProfileParametersFragment(): ProfileParameterFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMainParametersFragment(): MainParametersFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeFiltersFragment(): FilterFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeProjectEditFragment(): ProjectEditFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeProjectDetailFragment(): ProjectDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeAuthFragment(): AuthFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegistrationFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeProfileEditFragment(): ProfileEditFragment
}