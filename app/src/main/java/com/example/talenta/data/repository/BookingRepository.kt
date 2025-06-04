package com.example.talenta.data.repository

import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.BookingStatus
import com.example.talenta.data.model.PaymentStatus
import com.example.talenta.data.model.Role
import com.example.talenta.utils.FirestoreResult
import com.example.talenta.utils.safeFirebaseCall
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query.Direction
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    @Named("bookings") private val bookingCollection: CollectionReference,
    private val userPreferences: UserPreferences
) {

    suspend fun createBooking(
        expertId: String,
        serviceId: String,
        scheduleStartTime: String,
        hours: String
    ): FirestoreResult<Unit> = safeFirebaseCall {
        val userId = userPreferences.getUserData()?.id ?: ""
        val bookingId = bookingCollection.document().id
        val booking = Booking(
            bookingId = bookingId,
            artistId = userId,
            expertId = expertId,
            status = BookingStatus.PENDING,
            serviceId = serviceId,
            scheduledStartTime = scheduleStartTime,
            timeInHrs = hours.toInt(),
            paymentStatus = PaymentStatus.PENDING,
            createdAt = System.currentTimeMillis(),
        )
        val docRef = bookingCollection.document(booking.bookingId)
        docRef.set(booking).await()
        FirestoreResult.Success(Unit)
    }

    suspend fun getBookingsForUser(userId: String, role: Role): FirestoreResult<List<Booking>> =
        safeFirebaseCall {
            val field = if (role == Role.ARTIST) Booking::artistId.name else Booking::expertId.name
            val snapshot = bookingCollection
                .whereEqualTo(field, userId)
                .orderBy(Booking::createdAt.name, Direction.DESCENDING)
                .get().await()
            val bookings = snapshot.documents.mapNotNull { it.toObject(Booking::class.java) }
            bookings
        }

    suspend fun getBookingsById(bookingId: String): FirestoreResult<Booking?> =
        safeFirebaseCall {
            val snapshot = bookingCollection
                .whereEqualTo(Booking::bookingId.name, bookingId)
                .get().await()
            val bookings = snapshot.documents.mapNotNull { it.toObject(Booking::class.java) }
            bookings.firstOrNull()
        }

    suspend fun updateBookingStatus(
        bookingId: String, newStatus: BookingStatus
    ): FirestoreResult<Unit> = safeFirebaseCall {
        bookingCollection.document(bookingId).update(
            Booking::status.name, newStatus.name,
            Booking::updatedAt.name, System.currentTimeMillis()
        ).await()
        FirestoreResult.Success(Unit)
    }

    suspend fun updatePaymentStatus(
        bookingId: String, newStatus: PaymentStatus
    ): FirestoreResult<Unit> = safeFirebaseCall {
        bookingCollection.document(bookingId).update(
            Booking::paymentStatus.name, newStatus.name,
            Booking::updatedAt.name, System.currentTimeMillis()
        ).await()
        FirestoreResult.Success(Unit)
    }

    suspend fun changeStartDateTime(
        bookingId: String, newStartDateTime: String
    ): FirestoreResult<Unit> = safeFirebaseCall {
        bookingCollection.document(bookingId).update(
            Booking::scheduledStartTime.name, newStartDateTime,
            Booking::updatedAt.name, System.currentTimeMillis(),
            Booking::status.name, BookingStatus.RESCHEDULED
        ).await()
        FirestoreResult.Success(Unit)
    }

}