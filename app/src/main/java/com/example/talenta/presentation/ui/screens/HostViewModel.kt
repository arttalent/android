package com.example.talenta.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talenta.data.UserPreferences
import com.example.talenta.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HostViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    val userFlow = preferences.getUserDataFlow().stateIn<User?>(
        scope = viewModelScope,
        initialValue = null,
        started = SharingStarted.WhileSubscribed()
    )

}

