package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.User
import com.example.talenta.data.repository.ExpertRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

// UI State for Expert Profile
data class ExpertProfileUiState(
    val isLoading: Boolean = false,
    val expert: User? = null,
    val error: String? = null,
    val selectedMediaFilter: MediaFilter = MediaFilter.ALL
)

// Media filter options
enum class MediaFilter {
    ALL, PHOTOS, VIDEOS
}

@HiltViewModel
class ExpertViewModel @Inject constructor(
    private val repository: ExpertRepository
) : ViewModel() {

    private val _expert = MutableStateFlow<User?>(null)
    val expert: StateFlow<User?> = _expert.asStateFlow()

    private val _experts = MutableStateFlow<List<User>>(emptyList())
    val experts: StateFlow<List<User>> = _experts.asStateFlow()

    // Profile UI State for media handling
    private val _profileState = MutableStateFlow<ExpertProfileUiState>(ExpertProfileUiState())
    val profileState: StateFlow<ExpertProfileUiState> = _profileState.asStateFlow()

    init {
        fetchExperts()
    }

    fun getExpertById(expertId: String) {
        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true) }

            when (val result = repository.getExpertById(expertId)) {
                is FirestoreResult.Failure -> {
                    _profileState.update {
                        it.copy(
                            isLoading = false,
                            error = result.errorMessage ?: "Failed to load expert profile"
                        )
                    }
                }

                is FirestoreResult.Success -> {
                    _expert.value = result.data
                    _profileState.update {
                        it.copy(
                            isLoading = false,
                            expert = result.data,
                            error = null
                        )
                    }
                }
            }
        }
    }

    // Function to get current expert profile (similar to fetchArtistProfile)
    fun fetchExpertProfile() {
        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true) }

            repository.getCurrentUserProfile(true).collectLatest { user ->
                Timber.tag("TAG").d("fetchExpertProfile: $user")
                user?.let { expert ->
                    _profileState.update { state ->
                        state.copy(
                            isLoading = false,
                            expert = expert,
                            error = null
                        )
                    }
                } ?: run {
                    _profileState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = "Failed to load profile"
                        )
                    }
                }
            }
        }
    }

    // Function to handle media filter selection
    fun onMediaFilterSelected(filter: MediaFilter) {
        _profileState.update {
            it.copy(selectedMediaFilter = filter)
        }
    }

    private fun fetchExperts() {
        viewModelScope.launch {
            val result = repository.fetchExperts()
            when (result) {
                is FirestoreResult.Failure -> {
                    Timber.tag("TAG").d("fetchExperts Error : ${result.errorMessage}")
                    // Handle error
                }

                is FirestoreResult.Success -> {
                    Timber.tag("TAG").d("fetchExperts: ")
                    _experts.update {
                        result.data ?: emptyList()
                    }
                }
            }
        }
    }
}