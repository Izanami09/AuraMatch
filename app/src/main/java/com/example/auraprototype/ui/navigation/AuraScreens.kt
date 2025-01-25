package com.example.auraprototype.ui.navigation

sealed class AuraScreens (val route : String){
    data object SplashScreen : AuraScreens("splashScreen")

    data object PermissionScreen : AuraScreens("permissionScreen")

    data object GenderScreen : AuraScreens("genderScreen")
    data object FaceDetectionScreen : AuraScreens(route = "faceDetectionScreen")

    data object ClassificationScreen : AuraScreens(route = "classificationScreen")

    data object DetailsScreen : AuraScreens(route = "detailsScreen")
}