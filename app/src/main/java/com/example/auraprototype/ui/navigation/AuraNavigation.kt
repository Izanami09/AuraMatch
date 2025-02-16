package com.example.auraprototype.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.auraprototype.model.ClickedItem
import com.example.auraprototype.model.FilteredBread
import com.example.auraprototype.model.Recommendation

import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.FaceDetectionScreen
import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.ClassificationScreen
import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.DetailsScreen
import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.GenderScreen
import com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen.ImageCropperScreen
import com.example.auraprototype.ui.presentation.screen.splashScreen.LoginScreen
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
        composable(route = AuraScreens.LoginScreen.route) { LoginScreen(navController = navController) }
        composable(route = AuraScreens.GenderScreen.route) { GenderScreen(navController = navController) }
        composable(route = AuraScreens.PermissionScreen.route) { PermissionScreen(splashViewModel = hiltViewModel())  }
        composable(route = AuraScreens.ClassificationScreen.route) { ClassificationScreen(navController = navController) }
        composable(route = AuraScreens.FaceDetectionScreen.route){ FaceDetectionScreen(navController = navController)}
        composable(route = AuraScreens.DetailsScreen.route) { DetailsScreen(navController = navController) }
        composable(route = AuraScreens.ImageCropScreen.route) { ImageCropperScreen(navController = navController) }

    }
}