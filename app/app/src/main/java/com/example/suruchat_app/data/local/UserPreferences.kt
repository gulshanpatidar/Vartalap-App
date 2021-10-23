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
    }

    suspend fun saveUserLoginToken(token: String){
        context.dataStore.edit { preferences ->
            preferences[USER_LOGIN_TOKEN_KEY] = token
        }
    }

    val userLoginToken: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_LOGIN_TOKEN_KEY] ?: ""
        }
}