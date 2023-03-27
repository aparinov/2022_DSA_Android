package com.example.student_assistant

import android.app.Application
import com.example.student_assistant.di.component.AppComponent
import com.example.student_assistant.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector

class App : Application(), HasAndroidInjector {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = getApplicationComponent()
        appComponent.inject(this)
    }

    fun getApplicationComponent() = DaggerAppComponent.factory().create(this)

    override fun androidInjector(): AndroidInjector<Any> = appComponent.dispatchingAndroidInjector
}