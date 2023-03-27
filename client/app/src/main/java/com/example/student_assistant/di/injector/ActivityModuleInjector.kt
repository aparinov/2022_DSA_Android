package com.example.student_assistant.di.injector

import com.example.student_assistant.SampleActivity
import com.example.student_assistant.di.scope.ActivityScope
import com.example.student_assistant.ui.profile.ProfileActivity
import com.example.student_assistant.ui.project.ProjectActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModuleInjector {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): SampleActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeProfileActivity(): ProfileActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeProjectActivity(): ProjectActivity
}