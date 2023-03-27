package com.example.student_assistant.di.module

import android.content.Context
import com.example.student_assistant.data.local.AppDatabase
import com.example.student_assistant.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @Provides
    @AppScope
    fun providesDatabase(context: Context) = AppDatabase.getInstance(context)

    @Provides
    @AppScope
    fun providesCardDao(database: AppDatabase) = database.getCardDao()

    @Provides
    @AppScope
    fun providesProjectDao(database: AppDatabase) = database.getProjectDao()

    @Provides
    @AppScope
    fun providesUserDao(database: AppDatabase) = database.getUserDao()
}