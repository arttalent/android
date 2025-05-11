package com.example.talenta.presentation.expertAvailabilitySchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.DayOfWeek
import com.example.talenta.data.model.ExpertAvailability
import com.example.talenta.data.model.TimeSlot
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


sealed class ExpertAvailabilityScheduleActions {
    data class OnWeekdayDaySelect(val day: DayOfWeek) : ExpertAvailabilityScheduleActions()
    data class AddSlotByDay(val day: DayOfWeek, val timeSlot: TimeSlot) : ExpertAvailabilityScheduleActions()
    data object SetSchedule : ExpertAvailabilityScheduleActions()
    data object ResetError : ExpertAvailabilityScheduleActions()
}

data class ExpertAvailabilityScheduleState(
    val expertAvailability: ExpertAvailability? = null,
    val selectedDay: DayOfWeek? = null,
    val selectedDayAvailabilitySlot: List<TimeSlot> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel(assistedFactory = ExpertAvailabilityScheduleViewModelFactory::class)
class ExpertAvailabilityScheduleViewModel @AssistedInject constructor(
    private val expertRepository: ExpertRepository,
    @Assisted private val expertId: String,
) : ViewModel() {

    private val _uiStates = MutableStateFlow(ExpertAvailabilityScheduleState())
    val uiStates = _uiStates.asStateFlow()

    init {
        fetchExpertAvailability()
    }

    fun onAction(action: ExpertAvailabilityScheduleActions) {
        when (action) {
            ExpertAvailabilityScheduleActions.ResetError -> {
                _uiStates.update {
                    it.copy(
                        errorMessage = null
                    )
                }
            }

            is ExpertAvailabilityScheduleActions.OnWeekdayDaySelect -> {
                _uiStates.update {
                    it.copy(
                        selectedDay = action.day,
                        selectedDayAvailabilitySlot = it.expertAvailability?.weeklySchedule?.get(
                            action.day
                        )
                            ?: emptyList()
                    )
                }
            }

            is ExpertAvailabilityScheduleActions.AddSlotByDay -> {
                _uiStates.update {
                    val currentWeeklySchedule = it.expertAvailability?.weeklySchedule
                    currentWeeklySchedule?.get(action.day)?.toMutableList()?.add(action.timeSlot)
                    it.copy(
                        selectedDay = action.day,
                        selectedDayAvailabilitySlot = it.expertAvailability?.weeklySchedule?.get(
                            action.day
                        )
                            ?.plus(action.timeSlot)
                            ?: emptyList(),
                        expertAvailability = it.expertAvailability?.copy(
                            weeklySchedule = currentWeeklySchedule
                                ?: it.expertAvailability.weeklySchedule
                        )
                    )
                }
            }

            ExpertAvailabilityScheduleActions.SetSchedule -> {
                viewModelScope.launch {
                    _uiStates.update {
                        it.copy(
                            isLoading = true,
                        )
                    }
                    val result = expertRepository.setExpertAvailability(
                        expertId,
                        _uiStates.value.expertAvailability?.weeklySchedule
                            ?: emptyMap(),
                        _uiStates.value.expertAvailability?.timezone.toString()
                        )
                    when (result) {
                        is FirestoreResult.Success -> {
                            _uiStates.update {
                                it.copy(
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

}


@AssistedFactory
interface ExpertAvailabilityScheduleViewModelFactory {
    fun create(expertId: String): ExpertAvailabilityScheduleViewModel
}
