package com.example.auraprototype.ui.presentation.AuraUiStates

import com.google.accompanist.permissions.PermissionState

sealed class SplashUIState {
    object Loading : SplashUIState()
    object PermissionGranted : SplashUIState()
    object PermissionDenied : SplashUIState()
}

//data class SplashUIState (
//    val isLoading : Boolean = false,
//    val permissionGranted : Boolean = false,
//    var errorMessage :String? = null
//)