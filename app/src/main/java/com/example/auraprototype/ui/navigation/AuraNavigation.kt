package com.example.auraprototype.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.FaceDetectionScreen
import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.ClassificationScreen
import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.GenderScreen
import com.example.auraprototype.ui.presentation.screen.splashScreen.PermissionScreen
import com.example.auraprototype.ui.presentation.screen.splashScreen.SplashScreen

@Composable
fun AuraNavigation(){
        val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = AuraScreens.SplashScreen.route,
        route = "sharedGraph"
    ) {
        composable(route = AuraScreens.SplashScreen.route){ SplashScreen(navController = navController, splashViewModel = hiltViewModel())}
        composable(route = AuraScreens.GenderScreen.route) { GenderScreen(navController = navController) }
        composable(route = AuraScreens.PermissionScreen.route) { PermissionScreen(splashViewModel = hiltViewModel())  }
        composable(route = AuraScreens.ClassificationScreen.route) { ClassificationScreen(navController = navController) }
        composable(route = AuraScreens.FaceDetectionScreen.route){ FaceDetectionScreen(navController = navController)}

    }
}