//package com.example.talenta
//
//import com.example.talenta.data.model.Certificate
//import com.example.talenta.data.model.Expert
//import com.example.talenta.data.model.Person
//import com.example.talenta.data.model.Photo
//import com.example.talenta.data.model.Video
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext
//
//object Test {
//    private val firestore = FirebaseFirestore.getInstance()
//    private val expertsCollection = firestore.collection("experts")
//    suspend fun addMultipleDummyExperts() {
//        val dummyExperts = listOf(
//            Expert(
//                person = Person(
//                    id = "exp001", firstName = "Alice", lastName = "Smith",
//                    email = "alice.smith@example.com", profession = "Graphic Designer",
//                    subProfession = "UI/UX Designer", countryCode = "+1",
//                    mobileNumber = "9876543210", photoUrl = "https://example.com/alice.jpg",
//                    gender = "Female", age = 28, birthYear = 1996,
//                    language = "English, French", height = "168 cm",
//                    weight = "60 kg", ethnicity = "Caucasian", color = "Fair",
//                    city = "Los Angeles", country = "USA", bioData = "Creative UI/UX Designer"
//                ),
//                reviews = 5, // Changed to an Int as per your model
//                location = "Los Angeles, USA", // Changed to a String as per your model
//                rating = 4,
//                followers = 1200
//            ),
//            Expert(
//                person = Person(
//                    id = "exp002",
//                    firstName = "Bob",
//                    lastName = "Johnson",
//                    email = "bob.johnson@example.com",
//                    profession = "Software Engineer",
//                    subProfession = "Backend Developer",
//                    countryCode = "+44",
//                    mobileNumber = "123456789",
//                    photoUrl = "https://example.com/bob.jpg",
//                    gender = "Male",
//                    age = 32,
//                    birthYear = 1992,
//                    language = "English, German",
//                    height = "180 cm",
//                    weight = "75 kg",
//                    ethnicity = "African",
//                    color = "Dark",
//                    city = "London",
//                    country = "UK",
//                    bioData = "Backend engineer specializing in Java"
//                ),
//                reviews = 7, // Changed to an Int
//                location = "London, UK", // Changed to a String
//                rating = 5,
//                followers = 900
//            ),
//            Expert(
//                person = Person(
//                    id = "exp003",
//                    firstName = "Charlie",
//                    lastName = "Brown",
//                    email = "charlie.brown@example.com",
//                    profession = "Data Scientist",
//                    subProfession = "Machine Learning Engineer",
//                    countryCode = "+91",
//                    mobileNumber = "9876543210",
//                    photoUrl = "https://example.com/charlie.jpg",
//                    gender = "Male",
//                    age = 29,
//                    birthYear = 1995,
//                    language = "English, Hindi",
//                    height = "175 cm",
//                    weight = "72 kg",
//                    ethnicity = "Asian",
//                    color = "Medium",
//                    city = "Bangalore",
//                    country = "India",
//                    bioData = "ML Engineer with Python expertise"
//                ),
//                reviews = 10, // Changed to an Int
//                location = "Bangalore, India", // Changed to a String
//                rating = 5,
//                followers = 1100
//            )
//        )
//
//        for (expert in dummyExperts) {
//            val result = uploadExpert(expert)
//            if (result.isSuccess) {
//                println("Uploaded: ${expert.person.firstName} ${expert.person.lastName}")
//            } else {
//                println("Failed to upload: ${expert.person.firstName} ${expert.person.lastName}")
//            }
//        }
//    }
//
//    private suspend fun uploadExpert(expert: Expert): Result<Unit> = withContext(Dispatchers.IO) {
//        return@withContext try {
//            expertsCollection.document(expert.person.id).set(expert).await()
//            println("Successfully uploaded: ${expert.person.firstName} ${expert.person.lastName}")
//            Result.success(Unit)
//        } catch (e: Exception) {
//            println("Error uploading ${expert.person.firstName} ${expert.person.lastName}: ${e.localizedMessage}")
//            Result.failure(e)
//        }
//    }
//
//
//    suspend fun updateExpertWithDummyData(expert: Expert): Result<Unit> =
//        withContext(Dispatchers.IO) {
//            return@withContext try {
//                val updatedExpert = expert.copy(
//                    person = expert.person.copy(
//                        firstName = "John",
//                        lastName = "Doe",
//                        email = "john.doe@example.com",
//                        profession = "Software Developer",
//                        subProfession = "Android Developer",
//                        countryCode = "+1",
//                        mobileNumber = "9876543210",
//                        photoUrl = "https://example.com/john.jpg",
//                        gender = "Male",
//                        age = 30,
//                        birthYear = 1994,
//                        language = "English",
//                        height = "180 cm",
//                        weight = "75 kg",
//                        ethnicity = "Caucasian",
//                        color = "Fair",
//                        city = "New York",
//                        country = "USA",
//                        bioData = "Experienced Android Developer with 8 years in Kotlin and Java.",
//                        socialMediaLinks = expert.person.socialMediaLinks.copy(
//                            facebook = "https://facebook.com/johndoe",
//                            instagram = "https://instagram.com/johndoe",
//                            linkedin = "https://linkedin.com/in/johndoe",
//                            twitter = "https://twitter.com/johndoe"
//                        ),
//                        certificatesList = listOf(
//                            Certificate(
//                                id = "cert001",
//                                imageUrl = "https://example.com/cert1.jpg",
//                                description = "Kotlin Certified"
//                            ),
//                            Certificate(
//                                id = "cert002",
//                                imageUrl = "https://example.com/cert2.jpg",
//                                description = "Google AAD Certified"
//                            )
//                        ),
//                        photos = listOf(
//                            Photo(
//                                id = "photo001",
//                                imageUrl = "https://example.com/photo1.jpg",
//                                description = "At a tech conference"
//                            ),
//                            Photo(
//                                id = "photo002",
//                                imageUrl = "https://example.com/photo2.jpg",
//                                description = "Winning Hackathon"
//                            )
//                        ),
//                        videos = listOf(
//                            Video(
//                                id = "video001",
//                                videoUrl = "https://example.com/video1.mp4",
//                                thumbnailUrl = "https://example.com/thumb1.jpg",
//                                description = "Kotlin tutorial"
//                            ),
//                            Video(
//                                id = "video002",
//                                videoUrl = "https://example.com/video2.mp4",
//                                thumbnailUrl = "https://example.com/thumb2.jpg",
//                                description = "Jetpack Compose workshop"
//                            )
//                        ),
//                        skills = listOf("Kotlin", "Jetpack Compose", "MVVM", "Firestore")
//                    ),
//                    reviews = 12,
//                    location = "New York, USA",
//                    rating = 5,
//                    followers = 2500
//                )
//
//                expertsCollection.document(expert.person.id).set(updatedExpert).await()
//                println("Successfully updated expert: ${updatedExpert.person.firstName} ${updatedExpert.person.lastName}")
//                Result.success(Unit)
//            } catch (e: Exception) {
//                println("Error updating dummy expert: ${e.localizedMessage}")
//                Result.failure(e)
//            }
//        }
//
//}
