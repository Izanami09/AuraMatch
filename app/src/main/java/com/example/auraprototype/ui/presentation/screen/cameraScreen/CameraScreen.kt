package com.example.auraprototype.ui.presentation.screen.cameraScreen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.os.Build
import android.provider.MediaStore
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.navArgument

//main camera screen
@Composable
fun CameraScreen(
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry") navBackStackEntry: NavBackStackEntry = remember { navController.getBackStackEntry("sharedGraph") }
){
    val cameraViewModel : CameraViewModel = hiltViewModel(navBackStackEntry)
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    println(screenHeight)
    println(screenWidth)
    Box(modifier = Modifier.fillMaxSize()) {
        //Main Camera Preview
        CameraPreviewScreen(modifier = Modifier.fillMaxSize(), navController, cameraViewModel, screenWidth, screenHeight )
        // Overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Black.copy(alpha = 0.5f),
                size = size
            )

            // Define the face-safe area
            val safeAreaWidth = size.width * 0.6f
            val safeAreaHeight = size.height * 0.3f
            val safeAreaTopLeft = Offset(
                x = (size.width - safeAreaWidth) / 2,
                y = (size.height - safeAreaHeight) / 3
            )

            drawRect(
                color = Color.White,
                topLeft = safeAreaTopLeft,
                size = Size(safeAreaWidth, safeAreaHeight),
                blendMode = BlendMode.Clear
            )
        }

        // Text Prompt
        Text(
            text = "Please fit your face in the container",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }

}


@Composable
fun CameraPreviewScreen(
    modifier: Modifier,
    navController: NavController,
    cameraViewModel : CameraViewModel,
    screenWidth : Int,
    screenHeight : Int
) {
    //forBuilding Camera Selector
    val lensFacing  = CameraSelector.LENS_FACING_FRONT

    //for binding camera provider to lifecycle
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // (UI comp) display live camera feed on the screen
    val previewView = remember{ PreviewView(context)} //instance of PreviewView for XML

    //which camera to use
    val cameraXSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    //CameraX use cases
    val imageCapture : ImageCapture = remember {
        ImageCapture
            .Builder()
            .setTargetRotation(Surface.ROTATION_90)
            .build()
    } // to build capture button //one of the use case
    val preview = Preview.Builder().build() //(Use case) captures frames from camera and send them to surface for rendering

    //asking for camera future
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    //1.Asking for camera and binding it to lifecycle
    LaunchedEffect(lensFacing){
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraXSelector, preview, imageCapture)

            //connects usecase Preview to ui  PreviewView
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }, ContextCompat.getMainExecutor(context)) //everthing on main UI thread
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ){

        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Button(onClick = { cameraViewModel.captureImage(
            imageCapture = imageCapture,
            context = context,
            navController = navController,
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            onImageCaptured = { bitmap -> cameraViewModel.processImage(bitmap) })

        }) {
            Text(text = "Capture Image")
        }
    }

}

