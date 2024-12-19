package com.example.auraprototype.ui.presentation.screen.cameraScreen

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraScreen(){
    Box(modifier = Modifier.fillMaxSize()) {

        CameraPreviewScreen(modifier = Modifier.fillMaxSize())
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
    modifier: Modifier
) {
    val lensFacing  = CameraSelector.LENS_FACING_FRONT
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember{ PreviewView(context)}

    val cameraXSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture : ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    LaunchedEffect(lensFacing){
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraXSelector, preview, imageCapture)
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }, ContextCompat.getMainExecutor(context))
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ){
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Button(onClick = { captureImage(imageCapture, context) }) {
            Text(text = "Capture Image")
        }
    }

}

private fun captureImage(imageCapture: ImageCapture, context: Context){
    val name = "CameraXImage.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Aura")
            }
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println("Success")
            }

            override fun onError(exception: ImageCaptureException) {
                println("Failed $exception")
            }
        }
    )
}