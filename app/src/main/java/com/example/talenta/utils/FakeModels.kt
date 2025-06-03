package com.example.talenta.utils

import com.example.talenta.data.model.Bio
import com.example.talenta.data.model.Booking
import com.example.talenta.data.model.BookingStatus
import com.example.talenta.data.model.Certificate
import com.example.talenta.data.model.DateSlot
import com.example.talenta.data.model.Ethnicity
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.Media
import com.example.talenta.data.model.MediaType
import com.example.talenta.data.model.PaymentStatus
import com.example.talenta.data.model.PhysicalAttributes
import com.example.talenta.data.model.ProfessionalData
import com.example.talenta.data.model.Role
import com.example.talenta.data.model.Schedule
import com.example.talenta.data.model.Service
import com.example.talenta.data.model.ServiceType
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.model.User

object FakeModels {

    val fakeUserArtist = User(
        id = "artist_123",
        firstName = "Priya",
        lastName = "Sharma",
        email = "priya.sharma@example.com",
        phoneNumber = "+91 9876543210",
        physicalAttributes = PhysicalAttributes(
            height = "5'6\"",
            weight = "55 kg",
            gender = "Female",
            age = 24,
            ethnicity = Ethnicity.SOUTH_ASIAN,
            color = "Brown"
        ),
        profilePicture = "https://example.com/profile/priya_sharma.jpg",
        bio = Bio(
            city = "Mumbai",
            country = "India",
            bioData = "Passionate singer and dancer with 8 years of experience in Bollywood music and classical Indian dance forms. Love performing fusion music that blends traditional and contemporary styles.",
            language = "Hindi, English, Marathi",
            socialMediaLinks = SocialMediaLinks(
                instagram = "@priya_music_official",
                facebook = "Priya Sharma Music",
                twitter = "@priyasharmamusic",
                linkedin = "priya-sharma-artist"
            )
        ),
        role = Role.ARTIST,
        isVerified = true,
        isBlocked = false,
        professionalData = ProfessionalData(
            profession = "Singer",
            subProfession = "Bollywood Playback Singer, Classical Vocalist",
            media = listOf(
                Media(
                    url = "https://example.com/videos/priya_performance1.mp4",
                    type = MediaType.VIDEO,
                    description = "Live performance at Mumbai Music Festival 2024",
                    timestamp = 1704067200000L // Jan 1, 2024
                ),
                Media(
                    url = "https://example.com/images/priya_studio.jpg",
                    type = MediaType.IMAGE,
                    description = "Recording session at Abbey Road Studios",
                    timestamp = 1709251200000L // Mar 1, 2024
                )
            ),
            skills = listOf(
                "Hindustani Classical Vocals",
                "Bollywood Singing",
                "Kathak Dance",
                "Contemporary Dance",
                "Stage Performance",
                "Music Composition"
            ),
            certifications = listOf(
                "Grade 8 Hindustani Vocals - Prayag Sangit Samiti",
                "Kathak Visharad - Gandharva Mahavidyalaya"
            ),
            certificatesList = listOf(
                Certificate(
                    id = "cert_001",
                    imageUrl = "https://example.com/certificates/hindustani_vocals.jpg",
                    description = "Grade 8 Hindustani Classical Vocals Certification",
                    timestamp = 1640995200000L // Jan 1, 2022
                ),
                Certificate(
                    id = "cert_002",
                    imageUrl = "https://example.com/certificates/kathak_dance.jpg",
                    description = "Kathak Dance Visharad Certification",
                    timestamp = 1672531200000L // Jan 1, 2023
                )
            )
        ),
        sponsorDetails = null,
        expertService = null
    )

    val fakeExpertUser = User(
        id = "expert_456",
        firstName = "Rajesh",
        lastName = "Kumar",
        email = "rajesh.kumar@musicacademy.com",
        phoneNumber = "+91 9123456789",
        physicalAttributes = PhysicalAttributes(
            height = "5'8\"",
            weight = "70 kg",
            gender = "Male",
            age = 35,
            ethnicity = Ethnicity.SOUTH_ASIAN,
            color = "Brown"
        ),
        profilePicture = "https://example.com/profile/rajesh_kumar.jpg",
        bio = Bio(
            city = "Delhi",
            country = "India",
            bioData = "Experienced music instructor and mentor with 12+ years in the industry. Specialized in vocal training, music theory, and artist development. Have trained over 200+ students including several professional artists.",
            language = "Hindi, English, Punjabi",
            socialMediaLinks = SocialMediaLinks(
                instagram = "@rajesh_music_guru",
                facebook = "Rajesh Kumar Music Academy",
                twitter = "@rajeshmusicedu",
                linkedin = "rajesh-kumar-music-expert"
            )
        ),
        role = Role.EXPERT,
        isVerified = true,
        isBlocked = false,
        professionalData = ProfessionalData(
            profession = "Music Instructor",
            subProfession = "Vocal Coach, Music Theory Teacher, Artist Mentor",
            media = listOf(
                Media(
                    url = "https://example.com/videos/rajesh_teaching_session.mp4",
                    type = MediaType.VIDEO,
                    description = "Vocal training masterclass - Breathing techniques",
                    timestamp = 1706659200000L // Jan 31, 2024
                ),
                Media(
                    url = "https://example.com/images/rajesh_classroom.jpg",
                    type = MediaType.IMAGE,
                    description = "Music theory class at Delhi Music Academy",
                    timestamp = 1709337600000L // Mar 2, 2024
                )
            ),
            skills = listOf(
                "Vocal Training",
                "Music Theory Teaching",
                "Artist Mentoring",
                "Performance Coaching",
                "Music Psychology",
                "Instrumental Teaching",
                "Career Guidance"
            ),
            certifications = listOf(
                "Master of Music - Delhi University",
                "Certified Vocal Coach - Berklee Online",
                "Music Therapy Certification - AMTA"
            ),
            certificatesList = listOf(
                Certificate(
                    id = "cert_expert_001",
                    imageUrl = "https://example.com/certificates/master_music.jpg",
                    description = "Master of Music Degree - Delhi University",
                    timestamp = 1483228800000L // Jan 1, 2017
                ),
                Certificate(
                    id = "cert_expert_002",
                    imageUrl = "https://example.com/certificates/vocal_coach.jpg",
                    description = "Certified Vocal Coach - Berklee College of Music",
                    timestamp = 1577836800000L // Jan 1, 2020
                )
            )
        ),
        sponsorDetails = null,
        expertService = listOf(
            Service(
                serviceId = "service_001",
                serviceType = ServiceType.LIVE_ASSESSMENT,
                perHourCharge = 24.99f,
                isActive = true,
                expertAvailability = ExpertAvailability(
                    timezone = "Asia/Kolkata",
                    schedule = listOf(
                        Schedule(
                            dateSlot = DateSlot(
                                startDateTime = "2024-01-15T10:00:00Z",
                                endDateTime = "2024-01-15T12:00:00Z"
                            ),
                            timeSlot = TimeSlot(
                                start = "10:00",
                                end = "12:00"
                            )
                        )
                    )
                )
            ),
            Service(
                serviceId = "service_004",
                serviceType = ServiceType.VIDEO_ASSESSMENT,
                perHourCharge = 24.99f,
                isActive = true,
                expertAvailability = ExpertAvailability(
                    timezone = "Asia/Kolkata",
                    schedule = listOf(
                        Schedule(
                            dateSlot = DateSlot(
                                startDateTime = "2024-01-15T10:00:00Z",
                                endDateTime = "2024-01-15T12:00:00Z"
                            ),
                            timeSlot = TimeSlot(
                                start = "10:00",
                                end = "12:00"
                            )
                        )
                    )
                )
            ),
        )
    )

    val fakeBooking = Booking(
        bookingId = "booking_789",
        expertId = fakeExpertUser.id.toString(),
        artistId = fakeUserArtist.id.toString(),
        serviceId = "service_001",
        scheduledStartTime = "2024-01-15T10:00:00Z", // UTC ISO 8601 format
        timeInHrs = 2,
        status = BookingStatus.PENDING,
        paymentStatus = PaymentStatus.NOT_PAID,
        report = null, // Expert assessment (structure to be defined)
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}