package com.example.auraprototype.ui.navigation

sealed class AuraScreens (val route : String){
    data object SplashScreen : AuraScreens("splashScreen")

    data object PermissionScreen : AuraScreens("permissionScreen")
    data object CameraScreen : AuraScreens(route = "cameraScreen")

    data object ClassificationScreen : AuraScreens(route = "classificationScreen")
}