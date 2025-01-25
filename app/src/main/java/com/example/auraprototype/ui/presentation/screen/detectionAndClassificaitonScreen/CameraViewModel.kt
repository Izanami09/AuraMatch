package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
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

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
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
        faceShape : String = _cameraScreenState.value.faceShape,
        gender : String = _cameraScreenState.value.faceShape
    ){
        viewModelScope.launch{
            _classificationUiState.value =  Resource.Loading()
            val recommendations = withContext(Dispatchers.IO){recommendationRepository.getRecommendations(faceShape, gender)}
            _classificationUiState.value = recommendations
        }
    }


    //toProcess and send Face data to remote api
    fun processImage(bitmap: Bitmap, navController: NavController){
        _cameraScreenState.update {
            it.copy(
                image = bitmap
            )
        }
        println(bitmap)
        val result = classifyFaceShape.classifyFaceShape(bitmap)
        println(result)
        _cameraScreenState.update {
            it.copy(
                    faceShape = result
            )

        }
        navController.navigate("classificationScreen")
    }

    //on Male Button Clicked
    fun maleButtonClicked() {
        _cameraScreenState.update {
            it.copy(
                gender = "male"
            )
        }
    }
    //on Male Button Clicked
    fun femaleButtonClicked() {
        _cameraScreenState.update {
            it.copy(
                gender = "female"
            )
        }
    }

    //
 fun capturePhotoAndReplaceBackground(
        context: Context,
        cameraController: LifecycleCameraController,
        isFaceDetected: Boolean,
        onBackgroundReplaced: (Bitmap) -> Unit,
        toNavigate : (processedBitmap : Bitmap) -> Unit
    ) {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
        if (isFaceDetected) {
            cameraController.takePicture(
                mainExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val capturedBitmap: Bitmap =
                            image.toBitmap().rotateBitmap(image.imageInfo.rotationDegrees)

                        processCapturedPhotoAndReplaceBackground(capturedBitmap) { processedBitmap ->
                            onBackgroundReplaced(processedBitmap)
                            toNavigate(processedBitmap)
                        }

                        image.close()

                    }

                    override fun onError(exception: ImageCaptureException) {
                    }
                },
            )
        }
    }

    private fun processCapturedPhotoAndReplaceBackground(
        capturedBitmap: Bitmap,
        onBackgroundReplaced: (Bitmap) -> Unit,
    ) {
        onBackgroundReplaced(capturedBitmap)
    }

    fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
        val matrix = Matrix().apply {
            postRotate(-rotationDegrees.toFloat())
            postScale(1f, -1f)
        }

        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }



}

