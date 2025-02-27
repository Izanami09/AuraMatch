package com.example.auraprototype.ui.presentation.screen.tryon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.auraprototype.R
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.face.Face
import androidx.camera.core.Preview
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalGetImage::class)
@Composable
fun TryOnScreen() {
    // Directly call the ViewModel here
    val tryOnViewModel: TryOnViewModel = hiltViewModel()

    // Combine Camera Preview and Hairstyle Try-On logic
    TryOnScreenCameraPreview(tryOnViewModel)
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun TryOnScreenCameraPreview(tryOnViewModel: TryOnViewModel) {
    val context = LocalContext.current
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val previewUseCase = remember { androidx.camera.core.Preview.Builder().build() }
    val imageAnalysisUseCase = remember {
        ImageAnalysis.Builder().build().apply {
            setAnalyzer(ContextCompat.getMainExecutor(context), tryOnViewModel.imageAnalyzer)
        }
    }

    // Rebind camera provider when it's ready
    fun rebindCameraProvider() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT) // Front camera
                .build()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                previewUseCase,
                imageAnalysisUseCase
            )
        }
    }

    LaunchedEffect(Unit) {
        cameraProvider = cameraProviderFuture.get()
        rebindCameraProvider()
    }

    // Display Camera Preview
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            PreviewView(context).also {
                previewUseCase.setSurfaceProvider(it.surfaceProvider)
                rebindCameraProvider()
            }
        }
    )

    // Combine Face Detection and Hairstyle try-on logic here
    HairstyleTryOnScreen(tryOnViewModel)
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun HairstyleTryOnScreen(tryOnViewModel: TryOnViewModel) {
    // Load hairstyle image once
    val hairstyleBitmap: Bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.haristyle) // Replace with your hairstyle image

    // Observe the face data from the ViewModel
    val faces by tryOnViewModel.detectedFaces.collectAsState(initial = emptyList())

    // Call function to display the hairstyle try-on on detected faces
    TryOnScreenWithHairStyle(faces, hairstyleBitmap)
}

@Composable
fun TryOnScreenWithHairStyle(faces: List<Face>, hairstyleBitmap: Bitmap) {
    val hairstyleImage = hairstyleBitmap.asImageBitmap()

    Canvas(modifier = Modifier.fillMaxSize()) {
        faces.forEach { face ->
            val faceBox = face.boundingBox

            // Calculate scaling based on face width
            val scale = faceBox.width().toFloat() / hairstyleImage.width

            // Calculate position (centered horizontally, above head)
            val x = faceBox.left + (faceBox.width() - hairstyleImage.width * scale) / 2
            val y = faceBox.top - hairstyleImage.height * scale

            withTransform({
                translate(left = x, top = y)
                scale(scaleX = scale, scaleY = scale)
            }) {
                drawImage(
                    image = hairstyleImage,
                    srcOffset = IntOffset.Zero,
                    srcSize = IntSize(hairstyleImage.width, hairstyleImage.height)
                )
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@androidx.compose.ui.tooling.preview.Preview
@Composable
fun TryOnScreenPreview() {
    TryOnScreen()
}
