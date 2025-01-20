package com.example.auraprototype.ui.presentation.screen.splashScreen

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.auraprototype.ui.presentation.AuraUiStates.SplashUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel () {
    private val _splashUiState = MutableStateFlow<SplashUIState>(SplashUIState.Loading)

    val splashUIState : StateFlow<SplashUIState> = _splashUiState

   fun checkPermission(context: Context){
        viewModelScope.launch {

            val permissionStatus = ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.CAMERA
            )
            if (permissionStatus == PackageManager.PERMISSION_GRANTED){
                _splashUiState.value = SplashUIState.PermissionGranted
            }else{
                _splashUiState.value = SplashUIState.PermissionDenied
            }
        }
    }

}