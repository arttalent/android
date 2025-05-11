package com.example.talenta.data.repository

import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.BookingStatus
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    @Named("bookings")
    private val bookingCollection: CollectionReference,
    private val userPreferences: UserPreferences
) {

    suspend fun createBooking(booking: Booking): FirestoreResult<Unit> = safeFirebaseCall {
        val docRef = bookingCollection.document(booking.bookingId)
        docRef.set(booking).await()
        FirestoreResult.Success(Unit)
    }

    suspend fun getBookingsForUser(userId: String, role: String): FirestoreResult<List<Booking>> =
        safeFirebaseCall {
            val field = if (role == "ARTIST") "artistId" else "expertId"
            val snapshot = bookingCollection
                .whereEqualTo(field, userId)
                .get()
                .await()

            val bookings = snapshot.documents.mapNotNull { it.toObject(Booking::class.java) }
            bookings
        }

    suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: BookingStatus
    ): FirestoreResult<Unit> =
        safeFirebaseCall {
            bookingCollection.document(bookingId).update("status", newStatus.name).await()
            FirestoreResult.Success(Unit)
        }

}