package com.example.popmovie

import com.airbnb.mvrx.Mavericks
import android.app.Application as AndroidApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : AndroidApplication() {
    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
    }
}
