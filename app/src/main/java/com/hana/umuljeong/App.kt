package com.hana.umuljeong

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        var ACCESS_TOKEN: String = ""
    }
}