package com.example.apporder

import android.app.Application
import android.content.Context
import com.example.apporder.room.AppDao
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var appDao: AppDao

    companion object {

        private lateinit var app: App

        fun instance(): App {
            return app
        }

        fun appContext(): Context = instance().applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}