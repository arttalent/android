package com.example.talenta.navigation.Routes

import com.example.talenta.data.model.Role
import kotlinx.serialization.Serializable

@Serializable
sealed class Route() {
    @Serializable
    object AuthGraph : Route()

    @Serializable
    object ServiceTab : Route()

    @Serializable
    object CreateServiceScreen : Route()

    @Serializable
    object HostGraph : Route()

    // Inside Auth Graph
    @Serializable
    object Onboarding : Route()

    @Serializable
    object Auth : Route()

    @Serializable
    object SignUpAs : Route()

    @Serializable
    data class SignUp(val role: Role) : Route()

    @Serializable
    object OTPVerification : Route()

    @Serializable
    object Success : Route()

    @Serializable
    object Login : Route()

    @Serializable
    object ForgotPassword : Route()

    @Serializable
    object PasswordResetSuccess : Route()

    // Inside Bottom Nav Graph`
    @Serializable
    object Dashboard : Route()


    @Serializable
    object ExpertDashboard : Route()

    @Serializable
    object Experts : Route()

    @Serializable
    object MyBookings : Route()

    @Serializable
    object Notice : Route()

    @Serializable
    object Profile : Route()

    @Serializable
    object EditProfile : Route()

    // sponsor
    @Serializable
    object SponsorDashboard : Route()
    @Serializable
    object SponsorExpert : Route()
    @Serializable
    object SponsorArtist : Route()

    @Serializable
    object SponsorApplication : Route()

    @Serializable
    object SponsorProfile : Route()


    object Routes {
        private const val ExpertBooking = "expert_booking"
        fun withArgs(expertId: String, serviceId: String): String =
            "$ExpertBooking/$expertId/$serviceId"
    }


    @Serializable
    data class ExpertDetail(val expertId: String) : Route()

    @Serializable
    data class ExpertAvailabilitySetScreen(val expertId: String) : Route()

    @Serializable
    data class ExpertBookingScreen(val expertId: String, val serviceId: String) : Route()


}