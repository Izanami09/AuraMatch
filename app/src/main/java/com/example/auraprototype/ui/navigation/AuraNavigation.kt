package com.example.auraprototype.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auraprototype.ui.presentation.screen.cameraScreen.CameraScreen
import com.example.auraprototype.ui.presentation.screen.splashScreen.SplashScreen

@Composable
fun AuraNavigation(){
        val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = AuraScreens.SplashScreen.route){
        composable("splashScreen"){ SplashScreen(navController = navController)}
        composable(route = "cameraScreen") { CameraScreen()  }
    }
}