package com.example.student_assistant.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.student_assistant.data.local.entity.CardDB
import com.example.student_assistant.data.local.entity.ProjectDB
import com.example.student_assistant.data.local.entity.UserDB
import com.example.student_assistant.data.local.converter.DBConverter

@Database(entities = [UserDB::class, ProjectDB::class, CardDB::class], version = 1)
@TypeConverters(DBConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "STUDENT_ASSISTANT_DB"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }

    abstract fun getProjectDao(): ProjectDao

    abstract fun getUserDao(): UserDao

    abstract fun getCardDao(): CardDao
}