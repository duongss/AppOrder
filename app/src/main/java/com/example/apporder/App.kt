package com.example.apporder

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
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