package com.example.auraprototype.ui.presentation.screen.classificationScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.presentation.screen.cameraScreen.CameraViewModel

@Composable
fun ClassificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry") navBackStackEntry: NavBackStackEntry = remember { navController.getBackStackEntry("sharedGraph") }
){
    val cameraViewModel: CameraViewModel = hiltViewModel(navBackStackEntry)
    val cameraUiState by cameraViewModel.cameraScreenState.collectAsState()

    val bitmap = cameraUiState.imageBitmap
    println("The value in screen is ${bitmap}")
    if (cameraUiState.imageBitmap != null) {
        ResizedImageView(image = cameraUiState.imageBitmap!!, cameraUiState.faceShape)
    }
    else{
        Text(text = "The bitmap value is null" )
        }
    }


@Composable
fun ResizedImageView(image: Bitmap, faceShape : String) {
    val resizedImage = Bitmap.createScaledBitmap(image, 224, 224, true)
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = BitmapPainter(resizedImage.asImageBitmap()),
            contentDescription = "Resized Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(text = faceShape)
    }

}