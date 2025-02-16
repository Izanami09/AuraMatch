package com.example.auraprototype.ui.presentation.AuraUiStates

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import com.example.auraprototype.model.ClickedItem
import com.example.auraprototype.model.FilteredBread
import com.example.auraprototype.model.FilteredGlass
import com.example.auraprototype.model.FilteredHair
import com.example.auraprototype.model.Recommendation
import com.example.auraprototype.model.Resource
import com.google.gson.annotations.SerializedName



sealed class ClassificationUIState{
    data object Loading : ClassificationUIState()
    data class Success(val recommendation: Recommendation) : ClassificationUIState()
    data class Error(val message: String) : ClassificationUIState()
}


data class CameraUiState(
    val image : Bitmap? = null,
    val faceShape : String = "",
    var gender : String = "",
    val clickedItem: ClickedItem = ClickedItem(),
    var isContentAlreadyFetched : Boolean = false,
    var lastFetchedFaceShape: String? = null, // Track last fetched face shape
    var selectedService : String = ""
)








