package com.example.talenta.data.repository

import com.example.talenta.data.model.Certificate
import com.example.talenta.data.model.Expert
import com.example.talenta.data.model.Person
import com.example.talenta.data.model.Photo
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.data.model.Video
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ExpertScreenRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {


    suspend fun fetchExperts(onResult: (List<Expert>) -> Unit) = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("experts").get().await()

            val expertList = snapshot.documents.mapNotNull { it.toObject(Expert::class.java) }

            onResult(expertList)
        } catch (e: Exception) {
            Timber.tag("Firestore").e(e, "Error fetching experts")
            onResult(emptyList())
        }
    }

    private fun DocumentSnapshot.toExpert(): Expert? = runCatching {
        Expert(
            id = getString("id") ?: "",
            person = this.toPerson(),
            reviews = getLong("reviews")?.toInt() ?: 0,
            location = getString("location") ?: "",
            rating = getLong("rating")?.toInt() ?: 0,
            followers = getLong("followers") ?: 0
        )
    }.getOrNull()

    suspend fun getExpertById(expertId: String): Expert? = withContext(Dispatchers.IO) {
        runCatching {
            firestore.collection("experts").document(expertId).get()
                .await().toObject(Expert::class.java)
        }.getOrNull()
    }


    private fun DocumentSnapshot.toPerson(): Person =
        Person(firstName = getString("firstName") ?: "",
            lastName = getString("lastName") ?: "",
            email = getString("email") ?: "",
            password = getString("password") ?: "",
            profession = getString("profession") ?: "",
            subProfession = getString("subProfession") ?: "",
            countryCode = getString("countryCode") ?: "",
            mobileNumber = getString("mobileNumber") ?: "",
            photoUrl = getString("photoUrl") ?: "",
            gender = getString("gender") ?: "",
            age = getLong("age")?.toInt() ?: 0,
            birthYear = getLong("birthYear")?.toInt() ?: 0,
            language = getString("language") ?: "",
            height = getString("height") ?: "",
            weight = getString("weight") ?: "",
            ethnicity = getString("ethnicity") ?: "",
            color = getString("color") ?: "",
            city = getString("city") ?: "",
            country = getString("country") ?: "",
            bioData = getString("bioData") ?: "",
            socialMediaLinks = toObject(SocialMediaLinks::class.java) ?: SocialMediaLinks(),
            certificatesList = (get("certificatesList") as? List<Map<String, Any>>)?.mapNotNull { it.toCertificate() }
                ?: emptyList(),
            photos = (get("photos") as? List<Map<String, Any>>)?.mapNotNull { it.toPhoto() }
                ?: emptyList(),
            videos = (get("videos") as? List<Map<String, Any>>)?.mapNotNull { it.toVideo() }
                ?: emptyList(),
            skills = (get("skills") as? List<String>) ?: emptyList())


    private inline fun <reified T> Map<String, Any>.toObject(): T? {
        return runCatching { Gson().fromJson(Gson().toJson(this), T::class.java) }.getOrNull()
    }

    private fun Map<String, Any>.toCertificate(): Certificate? = toObject()
    private fun Map<String, Any>.toPhoto(): Photo? = toObject()
    private fun Map<String, Any>.toVideo(): Video? = toObject()

}
