package com.example.talenta.utils

import android.app.Activity
import android.view.WindowManager
import com.example.talenta.data.model.Bio
import com.example.talenta.data.model.PhysicalAttributes
import com.example.talenta.data.model.ProfessionalData
import com.example.talenta.data.model.SocialMediaLinks
import com.example.talenta.data.model.User
import com.example.talenta.presentation.state.EditProfileState

object HelperFunctions {


    fun setScreenshotRestriction(activity: Activity, shouldRestrict: Boolean) {
        if (shouldRestrict) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }


    fun User.merge(state: EditProfileState.Success): User {
        return this.copy(
            firstName = state.firstName,
            lastName = state.lastName,
            email = state.email,
            phoneNumber = state.phoneNumber,
            profilePicture = state.profilePicture,
            role = state.role,
            isVerified = state.isVerified,
            isBlocked = state.blocked,
            physicalAttributes = PhysicalAttributes(
                height = state.height,
                weight = state.weight,
                gender = state.gender,
                age = state.age,
                ethnicity = state.ethnicity,
                color = state.color
            ),
            bio = Bio(
                city = state.city,
                country = state.country,
                bioData = state.bioData,
                language = state.language,
                socialMediaLinks = SocialMediaLinks(
                    facebook = state.facebook,
                    instagram = state.instagram,
                    linkedin = state.linkedin,
                    twitter = state.twitter
                )
            ),
            professionalData = ProfessionalData(
                profession = state.profession,
                subProfession = state.subProfession,
                media = state.media,
                skills = state.skills,
                certifications = state.certifications,
                certificatesList = state.certificatesList
            )
        )
    }

    fun mergeUser(existing: User, updated: User): User {
        return existing.copy(
            firstName = updated.firstName.takeIf { it.isNotBlank() } ?: existing.firstName,
            lastName = updated.lastName.takeIf { it.isNotBlank() } ?: existing.lastName,
            email = updated.email.takeIf { it.isNotBlank() } ?: existing.email,
            phoneNumber = updated.phoneNumber.takeIf { it.isNotBlank() } ?: existing.phoneNumber,
            profilePicture = updated.profilePicture.takeIf { it.isNotBlank() }
                ?: existing.profilePicture,

            physicalAttributes = existing.physicalAttributes.copy(
                height = updated.physicalAttributes.height.takeIf { it.isNotBlank() }
                    ?: existing.physicalAttributes.height,
                weight = updated.physicalAttributes.weight.takeIf { it.isNotBlank() }
                    ?: existing.physicalAttributes.weight,
                gender = updated.physicalAttributes.gender.takeIf { it.isNotBlank() }
                    ?: existing.physicalAttributes.gender,
                age = updated.physicalAttributes.age.takeIf { it != 0 }
                    ?: existing.physicalAttributes.age,
                ethnicity = updated.physicalAttributes.ethnicity
                    ?: existing.physicalAttributes.ethnicity,
                color = updated.physicalAttributes.color.takeIf { it.isNotBlank() }
                    ?: existing.physicalAttributes.color
            ),

            bio = existing.bio.copy(
                city = updated.bio.city.takeIf { it.isNotBlank() } ?: existing.bio.city,
                country = updated.bio.country.takeIf { it.isNotBlank() } ?: existing.bio.country,
                bioData = updated.bio.bioData.takeIf { it.isNotBlank() } ?: existing.bio.bioData,
                language = updated.bio.language.takeIf { it.isNotBlank() } ?: existing.bio.language,
                socialMediaLinks = existing.bio.socialMediaLinks.copy(
                    facebook = updated.bio.socialMediaLinks.facebook.takeIf { it.isNotBlank() }
                        ?: existing.bio.socialMediaLinks.facebook,
                    instagram = updated.bio.socialMediaLinks.instagram.takeIf { it.isNotBlank() }
                        ?: existing.bio.socialMediaLinks.instagram,
                    linkedin = updated.bio.socialMediaLinks.linkedin.takeIf { it.isNotBlank() }
                        ?: existing.bio.socialMediaLinks.linkedin,
                    twitter = updated.bio.socialMediaLinks.twitter.takeIf { it.isNotBlank() }
                        ?: existing.bio.socialMediaLinks.twitter
                )
            ),

            professionalData = existing.professionalData.copy(
                profession = updated.professionalData.profession.takeIf { it.isNotBlank() }
                    ?: existing.professionalData.profession,
                subProfession = updated.professionalData.subProfession.takeIf { it.isNotBlank() }
                    ?: existing.professionalData.subProfession,
                media = updated.professionalData.media.takeIf { it.isNotEmpty() }
                    ?: existing.professionalData.media,
                skills = updated.professionalData.skills.takeIf { it.isNotEmpty() }
                    ?: existing.professionalData.skills,
                certifications = updated.professionalData.certifications.takeIf { it.isNotEmpty() }
                    ?: existing.professionalData.certifications,
                certificatesList = updated.professionalData.certificatesList.takeIf { it.isNotEmpty() }
                    ?: existing.professionalData.certificatesList
            ),

            // Preserving sensitive fields
            role = existing.role,
            isVerified = existing.isVerified,
            isBlocked = existing.isBlocked
        )
    }

    fun String.capitalizeFirstLetter(): String {
        return this.lowercase().replaceFirstChar { it.uppercaseChar() }
    }



}