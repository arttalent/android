package com.example.talenta.presentation.expertBooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.DayOfWeek
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.toDayOfWeek
import com.example.talenta.data.repository.ExpertRepository
import com.example.talenta.utils.FirestoreResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.datetime.toLocalDateTime
import java.time.temporal.ChronoUnit


sealed class ExpertAvailabilityActions {
    data class OnDateSelected(val date: LocalDate) : ExpertAvailabilityActions()
    data object ResetError : ExpertAvailabilityActions()


}

data class ExpertAvailabilityState(
    val expertAvailability: ExpertAvailability? = null,
    val selectedDate: LocalDate? = null,
    val selectedDateAvailability: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel(assistedFactory = ExpertAvailabilityViewModelFactory::class)
class ExpertAvailabilityViewModel @AssistedInject constructor(
    private val expertRepository: ExpertRepository,
    @Assisted private val expertId: String,
) : ViewModel() {

    private val _uiStates = MutableStateFlow(ExpertAvailabilityState())
    val uiStates = _uiStates.asStateFlow()

    init {
        fetchExpertAvailability()
    }

    fun onAction(action: ExpertAvailabilityActions) {
        when (action) {
            ExpertAvailabilityActions.ResetError -> {
                _uiStates.update {
                    it.copy(
                        errorMessage = null
                    )
                }
            }

            is ExpertAvailabilityActions.OnDateSelected -> {

            }
        }
    }

    fun fetchExpertAvailability() {
        viewModelScope.launch {
            _uiStates.update {
                it.copy(
                    isLoading = true,
                )
            }
            val result = expertRepository.getExpertAvailability(expertId)
            when (result) {
                is FirestoreResult.Success -> {
                    val availability = result.data
                    availability?.let {
                        _uiStates.value.selectedDate?.dayOfWeek?.let { it ->
                            val slots =
                                getConvertedHourlySlots(
                                    expertAvailability = availability,
                                    day = it.toDayOfWeek()
                                )
                            _uiStates.update {
                                it.copy(
                                    selectedDateAvailability = slots,
                                )
                            }
                        }
                    }
                    _uiStates.update {
                        it.copy(
                            expertAvailability = availability,
                            isLoading = false
                        )
                    }
                }

                is FirestoreResult.Failure -> {
                    val errorMessage = result.errorMessage
                    _uiStates.update {
                        it.copy(
                            errorMessage = errorMessage,
                            isLoading = false
                        )
                    }
                }
            }
        }

    }

    fun getConvertedHourlySlots(
        expertAvailability: ExpertAvailability,
        day: DayOfWeek
    ): List<String> {
        val slots = expertAvailability.weeklySchedule[day] ?: return emptyList()

        val expertTimeZone = TimeZone.of(expertAvailability.timezone)
        val today = Clock.System.now().toLocalDateTime(expertTimeZone).date

        val result = mutableListOf<String>()

        for (slot in slots) {
            var current = LocalTime.parse(slot.start)
            val end = LocalTime.parse(slot.end)

            while (current < end) {
                val expertDateTime = LocalDateTime(
                    year = today.year,
                    monthNumber = today.monthNumber,
                    dayOfMonth = today.dayOfMonth,
                    hour = current.hour,
                    minute = current.minute
                )
                val instant = expertDateTime.toInstant(expertTimeZone)

                val userDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

                val formatted = "%02d:%02d".format(userDateTime.hour, userDateTime.minute)
                result.add(formatted)

                current = current.addHours(1)
            }
        }

        return result
    }

    fun LocalTime.addHours(hoursToAdd: Int): LocalTime {
        val dummyDate = LocalDate(2000, 1, 1) // any safe dummy date
        val dummyDateTime = LocalDateTime(dummyDate, this)
        val updated =
            dummyDateTime.toJavaLocalDateTime().plus(hoursToAdd.toLong(), ChronoUnit.HOURS)
        return updated.toLocalTime().toKotlinLocalTime()
    }


}


@AssistedFactory
interface ExpertAvailabilityViewModelFactory {
    fun create(expertId: String): ExpertAvailabilityViewModel
}
