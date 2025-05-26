package com.example.talenta.navigation.navTypes

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.talenta.data.model.User
import kotlinx.serialization.json.Json

object UserNavType : NavType<User>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): User? {
        return bundle.getString(key)?.let { Json.decodeFromString<User>(it) }
    }

    override fun parseValue(value: String): User {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: User): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: User) {
        bundle.putString(key, Json.encodeToString(value))
    }
}
