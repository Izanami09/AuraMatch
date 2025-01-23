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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.auraprototype.data.FaceShapeRepository
import com.example.auraprototype.data.RecommendationRepository
import com.example.auraprototype.model.Recommendation
import com.example.auraprototype.model.Resource
import com.example.auraprototype.model.Resource.Loading
import com.example.auraprototype.ui.presentation.AuraUiStates.CameraUiState

import com.example.auraprototype.ui.presentation.AuraUiStates.ClassificationUIState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val classifyFaceShape : FaceShapeRepository,
    private val recommendationRepository: RecommendationRepository
) : ViewModel() {

    private val _cameraScreenState = MutableStateFlow(CameraUiState())
    val cameraScreenState = _cameraScreenState.asStateFlow()

    private val _classificationUiState = MutableStateFlow<Resource<Recommendation>>(Loading())
    val classificationUIState = _classificationUiState.asStateFlow()


    //to fetchRecommendations
    fun getRecommendationsForScreen(
        faceShape : String = _cameraScreenState.value.faceShape
    ){
        viewModelScope.launch {
            _classificationUiState.value =  Resource.Loading()
            val recommendations = recommendationRepository.getRecommendations(faceShape)
            _classificationUiState.value = recommendations
        }
    }


    //toProcess and send Face data to remote api
    fun processImage(bitmap: Bitmap){
        val result = classifyFaceShape.classifyFaceShape(bitmap)
        println(result)
        _cameraScreenState.update {
            it.copy(
                faceShape = result
            )
        }
    }




    //to rotate and crop bitmap
    fun rotateAndCropBitmap(bitmap: Bitmap, degrees: Float, x: Int, y: Int, width: Int, height: Int ): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        val rotatedBitmap =   Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return Bitmap.createBitmap(rotatedBitmap, x, y, width, height)
    }


    //to rotate bitmap
    fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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

                        val portraitBitmap = rotateImage(bitmap, 90f)
                        onImageCaptured(portraitBitmap)
                        _cameraScreenState.update { cameraUiState ->
                            cameraUiState.copy(
                                image = portraitBitmap
                            )
                        }
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