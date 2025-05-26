package com.example.talenta.presentation.expertAvailabilitySchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    data object SetSchedule : ExpertAvailabilityScheduleActions()
    data object ResetError : ExpertAvailabilityScheduleActions()
}

data class ExpertAvailabilityScheduleState(
    val expertAvailability: ExpertAvailability? = null,
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

            ExpertAvailabilityScheduleActions.SetSchedule -> {
            /*    viewModelScope.launch {
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
                }*/
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
