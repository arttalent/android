package com.example.talenta.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.Expert
import com.example.talenta.data.repository.ExpertScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpertViewModel @Inject constructor(
    private val repository: ExpertScreenRepository
) : ViewModel() {

    private val _expert = MutableStateFlow<Expert?>(null)
    val expert: StateFlow<Expert?> = _expert.asStateFlow()

    fun getExpertById(expertId: String) {
        viewModelScope.launch {
            _expert.value = repository.getExpertById(expertId)
        }
    }

    private val _experts = MutableStateFlow<List<Expert>>(emptyList())
    val experts: StateFlow<List<Expert>> = _experts.asStateFlow()

    init {
        fetchExperts()
    }

    private fun fetchExperts() {
        viewModelScope.launch {
            repository.fetchExperts { expertList ->
                _experts.value = expertList
            }
        }
    }
}
