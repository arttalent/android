package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.DayOfWeek
import com.example.talenta.data.model.TimeSlot
import com.example.talenta.data.repository.ExpertRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpertAvailabilityUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val weeklySchedule: Map<DayOfWeek, List<TimeSlot>> = emptyMap(),
    val selectedDayOfWeek: DayOfWeek? = null
)

sealed class ExpertAvailabilityEvents {
    data class SetAvailability(
        val expertId: String,
        val timezone: String
    ) : ExpertAvailabilityEvents()

    data class GetAvailability(val expertId: String) : ExpertAvailabilityEvents()

    data class UpdateWeeklySchedule(
        val timeSlot: TimeSlot
    ) : ExpertAvailabilityEvents()
}

@HiltViewModel
class ExpertAvailabilityViewModel @Inject constructor(
    private val repository: ExpertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpertAvailabilityUiState())
    val uiState: StateFlow<ExpertAvailabilityUiState> = _uiState

    fun onEvent(event: ExpertAvailabilityEvents) {
        when (event) {
            is ExpertAvailabilityEvents.SetAvailability -> {
                setExpertAvailability(event.expertId, event.timezone)
            }

            is ExpertAvailabilityEvents.GetAvailability -> {
                getExpertAvailability(event.expertId)
            }

            is ExpertAvailabilityEvents.UpdateWeeklySchedule -> {
                updateWeeklySchedule(event.timeSlot)
            }
        }
    }

    private fun updateWeeklySchedule(timeSlot: TimeSlot) {
        val weeklySchedule = _uiState.value.weeklySchedule.toMutableMap()
        val dayOfWeek = _uiState.value.selectedDayOfWeek
        val newTimeSlotList = arrayListOf<TimeSlot>()
        newTimeSlotList.addAll(weeklySchedule[dayOfWeek] ?: emptyList())
        newTimeSlotList.add(timeSlot)
        dayOfWeek?.let {
            weeklySchedule[it] = newTimeSlotList
        }

        _uiState.update {
            it.copy(
                weeklySchedule = weeklySchedule
            )
        }
    }

    fun setExpertAvailability(
        expertId: String,
        timezone: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val data = repository.setExpertAvailability(
                expertId,
                _uiState.value.weeklySchedule,
                timezone
            )
            when (data) {
                is FirestoreResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                is FirestoreResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            error = data.errorMessage,
                            isLoading = false
                        )
                    }
                }
            }
        }

    }

    fun getExpertAvailability(expertId: String) {
        viewModelScope.launch {
            val data = repository.getExpertAvailability(expertId)
            when (data) {
                is FirestoreResult.Success -> {
                    _uiState.update {
                        it.copy(
                            weeklySchedule = data.data?.weeklySchedule ?: emptyMap(),
                            isLoading = false
                        )
                    }
                }

                is FirestoreResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            error = data.errorMessage,
                            isLoading = false
                        )
                    }
                }
            }
        }

    }


}