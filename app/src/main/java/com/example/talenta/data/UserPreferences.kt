package com.example.talenta.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.talenta.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_DATA = stringPreferencesKey("user_data")

    }

    val roleFlow: Flow<String?> = dataStore.data.map { preferences -> preferences[USER_ROLE_KEY] }


    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] == true
    }

    suspend fun clearUserRole() {
        dataStore.edit { it.remove(USER_ROLE_KEY) }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun saveUserData(user: User) {
        val userJson = Json.encodeToString(user)
        dataStore.edit { preferences ->
            preferences[USER_DATA] = userJson
        }
    }

    suspend fun saveUserRole(role: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("user_role")] = role
        }
    }

    suspend fun getUserData(): User? {
        val preferences = dataStore.data.first()
        val userJson = preferences[USER_DATA]
        return userJson?.let { Json.decodeFromString<User>(it) }
    }

    fun getUserDataFlow(): Flow<User?> {
        return dataStore.data.map { preferences ->
            val userJson = preferences[USER_DATA]
            userJson?.let { Json.decodeFromString<User>(it) }
        }
    }

}