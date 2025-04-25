package com.example.talenta.data.repository

import com.example.talenta.data.UserPreferences
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    @Named("bookings")
    private val bookingCollection: CollectionReference,
    private val userPreferences: UserPreferences
){

}