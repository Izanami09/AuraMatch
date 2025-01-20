package com.example.auraprototype.ui.presentation.screen.splashScreen

import android.Manifest


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.auraprototype.ui.RequestPermission
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.presentation.AuraUiStates.SplashUIState
import kotlinx.coroutines.delay


@Composable
 fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel
) {
    val splashUIState by splashViewModel.splashUIState.collectAsState()



        val context = LocalContext.current


        LaunchedEffect(Unit) {

            delay(700)
            splashViewModel.checkPermission(context)
        }

        when(splashUIState) {
            is SplashUIState.Loading -> {
                CircularProgressIndicator()
            }

            is SplashUIState.PermissionGranted -> {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(AuraScreens.CameraScreen.route)
                }
            }

            is SplashUIState.PermissionDenied ->{
                    PermissionScreen(splashViewModel = splashViewModel)
                }
            }
 }




