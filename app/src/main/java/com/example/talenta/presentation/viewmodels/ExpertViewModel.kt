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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExpertViewModel @Inject constructor(
    private val repository: ExpertRepository
) : ViewModel() {

    private val _expert = MutableStateFlow<User?>(null)
    val expert: StateFlow<User?> = _expert.asStateFlow()

    private val _experts = MutableStateFlow<List<User>>(emptyList())
    val experts: StateFlow<List<User>> = _experts.asStateFlow()

    init {
        fetchExperts()
    }

    fun getExpertById(expertId: String) {
        viewModelScope.launch {
            when (val result = repository.getExpertById(expertId)) {
                is FirestoreResult.Failure -> {
                    // Handle error
                }

                is FirestoreResult.Success -> {
                    _expert.value = result.data
                }
            }
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
