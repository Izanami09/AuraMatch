package com.example.auraprototype.ui.presentation.AuraUiStates

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap

data class CameraUiState(
//    val isLoading: Boolean = false,
//    val result: String? = null,
//    val errorMessage: String? = null,
    var imageBitmap: Bitmap? = null,
    var faceShape : String = ""
)