package com.example.auraprototype.ui.presentation.screen.splashScreen

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.auraprototype.ui.RequestPermission


@Composable
 fun SplashScreen(
    navController: NavController
) {

    RequestPermission(
        permissionType = Manifest.permission.CAMERA,
        permission = "Camera",
        onPermissionGranted = { }
    )
    RequestPermission(
            permissionType = Manifest.permission.ACCESS_FINE_LOCATION,
            permission = "Location",
            onPermissionGranted = { navController.navigate("cameraScreen")}
    )
}



