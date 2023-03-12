package com.hana.fieldmate

import android.app.Application
import com.hana.fieldmate.data.local.DataStoreModule
import com.hana.fieldmate.data.local.UserInfo
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class App : Application() {

    private lateinit var dataStore: DataStoreModule

    companion object {
        private lateinit var app: App
        private lateinit var userInfo: UserInfo
        fun getInstance(): App = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        dataStore = DataStoreModule(this)

        updateUserInfo()
    }

    fun updateUserInfo() {
        runBlocking { userInfo = dataStore.getUserInfo().first() }
    }

    fun getUserInfo(): UserInfo {
        return userInfo
    }

    fun getDataStore(): DataStoreModule = dataStore
}