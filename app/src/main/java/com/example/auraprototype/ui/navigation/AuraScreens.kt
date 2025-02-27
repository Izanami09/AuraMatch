package com.example.auraprototype.ui.navigation

sealed class AuraScreens (val route : String){
    object LoginScreen : AuraScreens("loginScreen")
     object SplashScreen : AuraScreens("splashScreen")

    object PermissionScreen : AuraScreens("permissionScreen")

    object GenderScreen : AuraScreens("genderScreen")
    object FaceDetectionScreen : AuraScreens(route = "faceDetectionScreen")

    object ClassificationScreen : AuraScreens(route = "classificationScreen")

    object DetailsScreen : AuraScreens(route = "detailsScreen")
    object ImageCropScreen : AuraScreens(route = "imageCropScreen")
    object TryOnScreen : AuraScreens(route = "tryOnScreen")
}