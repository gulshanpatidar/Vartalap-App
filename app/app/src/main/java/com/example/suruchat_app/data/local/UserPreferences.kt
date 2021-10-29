package com.example.suruchat_app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
    }

    suspend fun saveUserLoginToken(token: String){
        context.dataStore.edit { preferences ->
            preferences[USER_LOGIN_TOKEN_KEY] = token
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
}