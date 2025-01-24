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
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.presentation.AuraUiStates.CameraUiState

import com.example.auraprototype.ui.presentation.AuraUiStates.ClassificationUIState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

}