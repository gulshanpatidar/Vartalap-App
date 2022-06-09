package com.example.suruchat_app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {



    companion object{
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
            name = "user_pref"
        )
        private val USER_LOGIN_TOKEN_KEY = stringPreferencesKey(name = "user_login_token")
        private val USER_ID = stringPreferencesKey(name = "user_id")
        private val USER_IMAGE = stringPreferencesKey(name = "user_image")
        private val USER_NAME = stringPreferencesKey(name = "user_name")
        private val PRIVATE_KEY = stringPreferencesKey(name = "private_key")
        private val LOGIN_TIME = longPreferencesKey(name = "login_time")
    }

    suspend fun saveUserLoginToken(token: String){
        context.dataStore.edit { preferences ->
            preferences[USER_LOGIN_TOKEN_KEY] = token
        }
    }

    suspend fun saveLoginTime(loginTime: Long){
        context.dataStore.edit {
            it[LOGIN_TIME] = loginTime
        }
    }

    suspend fun savePrivateKey(privateKey: String){
        context.dataStore.edit { preferences ->
            preferences[PRIVATE_KEY] = privateKey
        }
    }

    suspend fun saveUserName(userName: String){
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    suspend fun saveUserId(userId: String){
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveUserImage(userImage: String){
        context.dataStore.edit { preferences ->
            preferences[USER_IMAGE] = userImage
        }
    }

    val userLoginToken: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_LOGIN_TOKEN_KEY] ?: ""
        }

    val loginTime: Flow<Long> = context.dataStore.data
        .map {
            it[LOGIN_TIME] ?: 0
        }

    val userId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID] ?: ""
        }

    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME] ?: ""
        }

    val userImage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_IMAGE] ?: ""
        }

    val privateKey: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PRIVATE_KEY] ?: ""
        }
}