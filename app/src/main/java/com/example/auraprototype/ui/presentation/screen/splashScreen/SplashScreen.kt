package com.example.auraprototype.ui.presentation.screen.splashScreen

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.dotlottie.dlplayer.Mode
import com.example.auraprototype.ui.RequestPermission
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.presentation.AuraUiStates.SplashUIState
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.delay



@Composable
 fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel,
    modifier : Modifier = Modifier
) {
    val splashUIState by splashViewModel.splashUIState.collectAsState()



        val context = LocalContext.current


        LaunchedEffect(Unit) {
            //delay(3000)
            splashViewModel.checkPermission(context)
        }

        when(splashUIState) {
            is SplashUIState.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize()
                ) {
//                    DotLottieAnimation(
//                        //source = DotLottieSource.Url("https://lottiefiles-mobile-templates.s3.amazonaws.com/ar-stickers/swag_sticker_piggy.lottie"), // URL of .json or .lottie
//                        source = DotLottieSource.Asset("splashScreenAnimation.json"),
//                        autoplay = true,
//                        loop = true,
//                        speed = 3f,
//                        useFrameInterpolation = false,
//                        playMode = Mode.FORWARD,
//                        modifier = Modifier.fillMaxSize()
//                    )
                }
            }

            is SplashUIState.PermissionGranted -> {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(AuraScreens.GenderScreen.route)
                }
            }

            is SplashUIState.PermissionDenied ->{
                    PermissionScreen(splashViewModel = splashViewModel)
                }
            }
 }




