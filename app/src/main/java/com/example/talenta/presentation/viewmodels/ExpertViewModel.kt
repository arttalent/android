package com.example.talenta.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.model.User
import com.example.talenta.data.repository.ExpertRepository
import com.example.talenta.utils.FirestoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExpertViewModel @Inject constructor(
    private val repository: ExpertRepository
) : ViewModel() {

    private val _expert = MutableStateFlow<User?>(null)
    val expert: StateFlow<User?> = _expert.asStateFlow()

    fun getExpertById(expertId: String) {
        viewModelScope.launch {
            val result = repository.getExpertById(expertId)
            when (result) {
                is FirestoreResult.Failure -> {
                    // Handle error
                }

                is FirestoreResult.Success -> {
                    _expert.value = result.data
                }
            }
        }
    }

    private val _experts = MutableStateFlow<List<User>>(emptyList())
    val experts: StateFlow<List<User>> = _experts.asStateFlow()

    init {
        fetchExperts()
    }

    private fun fetchExperts() {
        viewModelScope.launch {
            val result = repository.fetchExperts()
            when (result) {
                is FirestoreResult.Failure -> {
                    Timber.tag("TAG").d("fetchExperts: ${result.errorMessage}")
                    // Handle error
                }

                is FirestoreResult.Success -> {
                    Timber.tag("TAG").d("fetchExperts: ")
                    _experts.value = result.data ?: emptyList()
                }
            }
        }
    }
}
