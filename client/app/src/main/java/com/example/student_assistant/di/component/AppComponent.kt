package com.example.student_assistant.di.component

import android.app.Application
import android.content.Context
import com.example.student_assistant.di.factory.ViewModelFactory
import com.example.student_assistant.di.injector.ActivityModuleInjector
import com.example.student_assistant.di.injector.FragmentModuleInjector
import com.example.student_assistant.di.module.DatabaseModule
import com.example.student_assistant.di.module.NetworkModule
import com.example.student_assistant.di.module.RepositoryModule
import com.example.student_assistant.di.scope.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ActivityModuleInjector::class,
        FragmentModuleInjector::class,
        RepositoryModule::class,
        DatabaseModule::class,
        NetworkModule::class,
    ]
)
interface AppComponent {

    val dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(application: Application)

    fun viewModelFactory(): ViewModelFactory
}