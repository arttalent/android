package com.example.talenta.navigation.navTypes

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.talenta.data.model.LocalBooking
import kotlinx.serialization.json.Json


object LocalBookingNavType : NavType<LocalBooking>(isNullableAllowed = false) {

   override fun get(bundle: Bundle, key: String): LocalBooking? {
       return bundle.getString(key)?.let { Json.decodeFromString<LocalBooking>(it) }
   }

   override fun parseValue(value: String): LocalBooking {
       return Json.decodeFromString(Uri.decode(value))
   }

   override fun serializeAsValue(value: LocalBooking): String {
       return Uri.encode(Json.encodeToString(value))
   }

   override fun put(bundle: Bundle, key: String, value: LocalBooking) {
       bundle.putString(key, Json.encodeToString(value))
   }
}