package com.example.auraprototype.ui.presentation.screen.cameraScreen

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.auraprototype.data.FaceShapeRepository
import com.example.auraprototype.ui.presentation.AuraUiStates.CameraUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.checkerframework.checker.units.qual.degrees
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val classifyFaceShape : FaceShapeRepository
) : ViewModel() {

    private val _cameraScreenState = MutableStateFlow(CameraUiState())
    val cameraScreenState = _cameraScreenState.asStateFlow()
    fun processImage(bitmap: Bitmap) : String {
        val result = classifyFaceShape.classifyFaceShape(bitmap)
        println(result)
        _cameraScreenState.update {
            it.copy(
                faceShape = result
            )
        }
      return "Success in classfication"
    }

    //to rotate bitmap
    fun rotateAndCropBitmap(bitmap: Bitmap, degrees: Float, x: Int, y: Int, width: Int, height: Int ): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        println(
            "x,y,right, bottom: $x $y $width $height"
        )
        val rotatedBitmap =   Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

        return Bitmap.createBitmap(
            rotatedBitmap,
            x,
            y,
            width,
            height
        )
    }

    fun rotateImage(bitmap:Bitmap, degrees: Float) : Bitmap{
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        val rotatedBitmap =   Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
        return rotatedBitmap
    }
    //to Capture Image
    fun captureImage(
        imageCapture: ImageCapture,
        context: Context,
        navController: NavController,
        onImageCaptured : (Bitmap) -> Unit,
        screenWidth : Int,
        screenHeight : Int
    ){
        val name = "CameraXImage_${System.currentTimeMillis()}.jpg"
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
                    val savedUri = outputFileResults.savedUri
                    println("Image saved at : $savedUri")
                    println("Success")

                    savedUri?.let {
                        val bitmap = context.contentResolver.openInputStream(it).use {
                                inputStream -> BitmapFactory.decodeStream(inputStream)

                        }

//                        // Screen dimensions
//                        val screenWidth = 392
//                        val screenHeight = 768
//
//// Bitmap dimensions
//                        val bitmapWidth = 1940
//                        val bitmapHeight = 2529
//
//// Scale factors
//                        val widthScale = bitmapWidth.toFloat() / screenWidth
//                        val heightScale = bitmapHeight.toFloat() / screenHeight
//
//// Safe area dimensions (from your UI logic)
//                        val safeAreaWidth = screenWidth * 0.3f
//                        val safeAreaHeight = screenHeight * 0.6f
//                        val safeAreaTopLeftX = (screenWidth +200 - safeAreaWidth) / 2
//                        val safeAreaTopLeftY = (screenHeight + 240 - safeAreaHeight) / 3
//
//// Map safe area to bitmap dimensions
//                        val safeAreaBitmapX = safeAreaTopLeftX * widthScale
//                        val safeAreaBitmapY = safeAreaTopLeftY * heightScale
//                        val safeAreaBitmapWidth = safeAreaWidth * widthScale
//                        val safeAreaBitmapHeight = safeAreaHeight * heightScale

                        //val portraitBitmap = rotateAndCropBitmap(bitmap, 90f, x = safeAreaBitmapX.toInt(), y = safeAreaBitmapY.toInt(), width = safeAreaBitmapWidth.toInt(), height = safeAreaBitmapHeight.toInt())
                        val portraitBitmap = rotateImage(bitmap, 270f)
                        onImageCaptured(portraitBitmap)
                        _cameraScreenState.update { cameraUiState ->
                            cameraUiState.copy(
                                imageBitmap = portraitBitmap
                            )

                        }
                        println("Sending the bitmap value $bitmap")
                        navController.navigate("classificationScreen")
                    }


                }

                override fun onError(exception: ImageCaptureException) {
                    println("Failed $exception")
                }
            }
        )
    }
}