package com.jcjiron.qrapp.presentation.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // SavedStateHandle keeps the selected tab across rotation and process death.
    val selectedTab: StateFlow<MainTab> = savedStateHandle.getStateFlow(KEY_TAB, MainTab.Scan)

    fun onTabSelected(tab: MainTab) {
        savedStateHandle[KEY_TAB] = tab
    }

    private companion object {
        const val KEY_TAB = "selected_tab"
    }
}
