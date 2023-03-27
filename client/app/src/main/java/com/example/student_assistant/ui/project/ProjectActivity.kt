package com.example.student_assistant.ui.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.student_assistant.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ProjectActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_layout)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}