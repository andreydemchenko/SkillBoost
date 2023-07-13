package com.dem.spoyersoccer

import android.app.Application
import androidx.room.Room
import com.dem.spoyersoccer.data.local.AppDatabase

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "my-database"
        ).build()
    }
}
