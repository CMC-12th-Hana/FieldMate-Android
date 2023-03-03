package com.hana.umuljeong.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hana.umuljeong.data.TokenManager.PreferenceKeys.ACCESS_TOKEN
import com.hana.umuljeong.data.TokenManager.PreferenceKeys.LOGIN_CHECK
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val LOGIN_CHECK = booleanPreferencesKey("login_check")
    }

    private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore("ACCESS_TOKEN")
    private val Context.loginCheckDataStore: DataStore<Preferences> by preferencesDataStore("LOGIN_CHECK")

    suspend fun saveAccessToken(accessToken: String) {
        context.tokenDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun deleteAccessToken() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
        }
    }

    suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        context.tokenDataStore.edit { prefs ->
            prefs[LOGIN_CHECK] = isLoggedIn
        }
    }

    fun getAccessToken(): Flow<String> {
        return context.tokenDataStore.data
            .map { prefs ->
                prefs[ACCESS_TOKEN] ?: ""
            }
    }

    fun getIsLoggedIn(): Flow<Boolean> {
        return context.loginCheckDataStore.data
            .map { prefs ->
                prefs[LOGIN_CHECK] ?: false
            }
    }
}