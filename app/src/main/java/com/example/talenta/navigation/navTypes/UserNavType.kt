package com.example.talenta.navigation.navTypes

import android.os.Bundle
import android.util.Base64
import androidx.navigation.NavType
import com.example.talenta.data.model.User
import kotlinx.serialization.json.Json

object UserNavType : NavType<User>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): User? {
        return bundle.getString(key)?.let {
            val decoded = String(Base64.decode(it, Base64.URL_SAFE))
            Json.decodeFromString<User>(decoded)
        }
    }

    override fun parseValue(value: String): User {
        val decoded = String(Base64.decode(value, Base64.URL_SAFE))
        return Json.decodeFromString(decoded)
    }

    override fun serializeAsValue(value: User): String {
        val json = Json.encodeToString(value)
        return Base64.encodeToString(json.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
    }

    override fun put(bundle: Bundle, key: String, value: User) {
        val json = Json.encodeToString(value)
        bundle.putString(key, Base64.encodeToString(json.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP))
    }
}
