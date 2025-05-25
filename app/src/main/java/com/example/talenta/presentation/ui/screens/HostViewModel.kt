package com.example.talenta.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HostViewModel @Inject constructor(
    val hostRepository: HostRepository, private val preferences: UserPreferences
) : ViewModel() {

    private val _role = MutableStateFlow<String?>(null)
    val role = _role.asStateFlow()


    init {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            getRole(userId)
        }
        readRoleFromPreferences()
    }


    private fun readRoleFromPreferences() {
        viewModelScope.launch {
            preferences.roleFlow.collect { savedRole ->
                _role.value = savedRole
            }
        }
    }

    fun getRole(userId: String) {
        viewModelScope.launch {
            val fetchedRole = hostRepository.getRole(userId)
            _role.value = fetchedRole
        }
    }

}

