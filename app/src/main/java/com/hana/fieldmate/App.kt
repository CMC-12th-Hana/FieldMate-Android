package com.hana.fieldmate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        var ACCESS_TOKEN: String = ""
    }
}