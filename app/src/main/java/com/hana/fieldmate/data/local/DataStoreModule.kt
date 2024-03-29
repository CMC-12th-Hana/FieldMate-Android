package com.hana.fieldmate.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.ACCESS_TOKEN
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.LAST_MESSAGE_ATTEMPT_TIME
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.MESSAGE_ATTEMPTS
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.REFRESH_TOKEN
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.USER_COMPANY_ID
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.USER_COMPANY_NAME
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.USER_ID
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.USER_JOIN_COMPANY_STATUS
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.USER_LOGIN_CHECK
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.USER_NAME
import com.hana.fieldmate.data.local.DataStoreModule.PreferenceKeys.USER_ROLE
import com.hana.fieldmate.data.remote.model.response.JoinCompanyStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserInfo(
    val companyId: Long,
    val userId: Long,
    val companyName: String,
    val joinCompanyStatus: JoinCompanyStatus,
    val userName: String,
    val userRole: String,
    val isLoggedIn: Boolean
)

class DataStoreModule(private val context: Context) {
    private object PreferenceKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        val USER_LOGIN_CHECK = booleanPreferencesKey("user_login_check")
        val USER_COMPANY_ID = longPreferencesKey("user_company_id")
        val USER_JOIN_COMPANY_STATUS = stringPreferencesKey("user_join_company_status")
        val USER_ID = longPreferencesKey("user_id")
        val USER_COMPANY_NAME = stringPreferencesKey("user_company_name")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_ROLE = stringPreferencesKey("user_role")

        val MESSAGE_ATTEMPTS = intPreferencesKey("login_attempts")
        val LAST_MESSAGE_ATTEMPT_TIME = longPreferencesKey("last_message_attempt_time")
    }

    private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore("TOKEN")
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore("USER")
    private val Context.messageDataStore: DataStore<Preferences> by preferencesDataStore("MESSAGE")

    fun getUserInfo(): Flow<UserInfo> {
        return context.userDataStore.data
            .map { prefs ->
                UserInfo(
                    companyId = prefs[USER_COMPANY_ID] ?: -1L,
                    userId = prefs[USER_ID] ?: -1L,
                    companyName = prefs[USER_COMPANY_NAME] ?: "",
                    joinCompanyStatus = if (prefs[USER_JOIN_COMPANY_STATUS] != null) {
                        JoinCompanyStatus.valueOf(prefs[USER_JOIN_COMPANY_STATUS]!!)
                    } else {
                        JoinCompanyStatus.PENDING
                    },
                    userName = prefs[USER_NAME] ?: "",
                    userRole = prefs[USER_ROLE] ?: "",
                    isLoggedIn = prefs[USER_LOGIN_CHECK] ?: false
                )
            }
    }

    suspend fun saveUserInfo(
        companyId: Long,
        userId: Long,
        companyName: String,
        joinCompanyStatus: JoinCompanyStatus,
        userName: String,
        userRole: String
    ) {
        context.userDataStore.edit { prefs ->
            prefs[USER_COMPANY_ID] = companyId
            prefs[USER_ID] = userId
            prefs[USER_COMPANY_NAME] = companyName
            prefs[USER_JOIN_COMPANY_STATUS] = joinCompanyStatus.name
            prefs[USER_NAME] = userName
            prefs[USER_ROLE] = userRole
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        if (accessToken.isNotEmpty()) {
            context.tokenDataStore.edit { prefs ->
                prefs[ACCESS_TOKEN] = accessToken
            }
            context.userDataStore.edit { prefs ->
                prefs[USER_LOGIN_CHECK] = true
            }
        }
    }

    suspend fun deleteAccessToken() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
        }
        context.userDataStore.edit { prefs ->
            prefs[USER_LOGIN_CHECK] = false
        }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        if (refreshToken.isNotEmpty()) {
            context.tokenDataStore.edit { prefs ->
                prefs[REFRESH_TOKEN] = refreshToken
            }
        }
    }

    suspend fun deleteRefreshToken() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN)
        }
        context.userDataStore.edit { prefs ->
            prefs[USER_LOGIN_CHECK] = false
        }
    }

    fun getAccessToken(): Flow<String> {
        return context.tokenDataStore.data
            .map { prefs ->
                prefs[ACCESS_TOKEN] ?: ""
            }
    }

    fun getRefreshToken(): Flow<String> {
        return context.tokenDataStore.data
            .map { prefs ->
                prefs[REFRESH_TOKEN] ?: ""
            }
    }

    suspend fun setMessageAttempts(attempts: Int) {
        context.messageDataStore.edit { prefs ->
            prefs[MESSAGE_ATTEMPTS] = attempts
        }
    }

    fun getMessageAttempts(): Flow<Int> {
        return context.messageDataStore.data
            .map { prefs ->
                prefs[MESSAGE_ATTEMPTS] ?: 0
            }
    }

    suspend fun setLastMessageAttemptTime(currentTime: Long) {
        context.messageDataStore.edit { prefs ->
            prefs[LAST_MESSAGE_ATTEMPT_TIME] = currentTime
        }
    }

    fun getLastMessageAttemptTime(): Flow<Long> {
        return context.messageDataStore.data
            .map { prefs ->
                prefs[LAST_MESSAGE_ATTEMPT_TIME] ?: System.currentTimeMillis()
            }
    }
}