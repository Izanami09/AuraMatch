package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
fun ImageCropperScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry")
    navBackStackEntry: NavBackStackEntry = remember { navController.getBackStackEntry("sharedGraph") }
) {
    val cameraViewModel: CameraViewModel = hiltViewModel(navBackStackEntry)
    val cameraUiState by cameraViewModel.cameraScreenState.collectAsState()
    val imageBitmap = cameraUiState.image!!

    val cropBoxSizeDp = 224.dp
    val density = LocalDensity.current
    val cropBoxSizePx = with(density) { cropBoxSizeDp.toPx() }

    var cropBoxTopLeft by remember { mutableStateOf<Offset?>(null) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .background(Color.Black)
            .onSizeChanged { canvasSize = it }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (canvasSize.width > 0) {
                            val scale = canvasSize.width / imageBitmap.width.toFloat()
                            val dragAmountImage = dragAmount / scale
                            val currentTopLeft = cropBoxTopLeft ?: Offset.Zero
                            val newTopLeft = currentTopLeft + dragAmountImage

                            val cropBoxSizeImage = cropBoxSizePx / scale
                            val clampedX = newTopLeft.x.coerceIn(0f, imageBitmap.width - cropBoxSizeImage)
                            val clampedY = newTopLeft.y.coerceIn(0f, imageBitmap.height - cropBoxSizeImage)
                            cropBoxTopLeft = Offset(clampedX, clampedY)
                        }
                        change.consume()
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val imageWidth = imageBitmap.width.toFloat()
            val imageHeight = imageBitmap.height.toFloat()

            val scale = canvasWidth / imageWidth
            val scaledImageHeight = imageHeight * scale
            val offsetY = (canvasHeight - scaledImageHeight) / 2f

            // Draw Image
            drawImage(
                image = imageBitmap.asImageBitmap(),
                dstOffset = IntOffset(0, offsetY.toInt()), // Position the image
                dstSize = IntSize(canvasWidth.toInt(), scaledImageHeight.toInt()) // Scale the image
            )


            val cropBoxSizeImage = cropBoxSizePx / scale
            if (cropBoxTopLeft == null) {
                cropBoxTopLeft = Offset(
                    (imageWidth - cropBoxSizeImage) / 2f,
                    (imageHeight - cropBoxSizeImage) / 2f
                )
            }

            val cropTopLeft = cropBoxTopLeft!!
            val cropBoxCanvasTopLeft = Offset(cropTopLeft.x * scale, cropTopLeft.y * scale) + Offset(0f, offsetY)

            drawRect(
                color = Color.White,
                topLeft = cropBoxCanvasTopLeft,
                size = Size(cropBoxSizePx, cropBoxSizePx),
                style = Stroke(width = 2.dp.toPx())
            )

            val third = cropBoxSizePx / 3f
            for (i in 1 until 3) {
                drawLine(
                    color = Color.White,
                    start = cropBoxCanvasTopLeft + Offset(i * third, 0f),
                    end = cropBoxCanvasTopLeft + Offset(i * third, cropBoxSizePx),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.White,
                    start = cropBoxCanvasTopLeft + Offset(0f, i * third),
                    end = cropBoxCanvasTopLeft + Offset(cropBoxSizePx, i * third),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                if (canvasSize.width > 0 && cropBoxTopLeft != null) {
                    val scale = canvasSize.width / imageBitmap.width.toFloat()
                    val cropBoxSizeImage = cropBoxSizePx / scale
                    val cropX = cropBoxTopLeft!!.x.toInt()
                    val cropY = cropBoxTopLeft!!.y.toInt()

                    val cropSize = cropBoxSizeImage.toInt().coerceAtMost(
                        imageBitmap.width - cropX
                    ).coerceAtMost(imageBitmap.height - cropY)

                    val croppedBitmap = Bitmap.createBitmap(
                        imageBitmap, cropX, cropY, cropSize, cropSize
                    )
                    val finalBitmap = Bitmap.createScaledBitmap(croppedBitmap, 224, 224, true)
                    cameraViewModel.processImage(finalBitmap, navController)
                }
            }) {
                Text("Crop Face")
            }
        }
    }
}
