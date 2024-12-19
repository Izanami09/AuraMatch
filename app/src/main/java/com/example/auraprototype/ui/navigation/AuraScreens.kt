package com.example.auraprototype.ui.navigation

sealed class AuraScreens (val route : String){
    data object SplashScreen : AuraScreens("splashScreen")
    data object CameraScreen : AuraScreens(route = "cameraScreen")
}