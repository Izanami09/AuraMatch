package com.example.auraprototype.ui.presentation.screen.tryon

import android.content.Context
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
@ExperimentalGetImage

class TryOnViewModel @Inject constructor() : ViewModel() {

    var detectedFaces  = MutableStateFlow<List<Face>>(emptyList())
    val imageAnalyzer = ImageAnalysis.Analyzer { imageProxy ->
        val mediaImage = imageProxy.image
        mediaImage?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            // Create an instance of the face detector
            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .build()
            val detector = FaceDetection.getClient(options)

            // Detect faces
            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    // Handle face detection result
                    // Use these faces to overlay the hairstyle image
                    detectedFaces.value = faces.filter { it.boundingBox.width() > 100 } // Only select faces large enough
                    // Save detected faces for UI
                    // For now, we'll use a MutableState for holding detected faces
                    // This state can be updated on the success listener
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
                .addOnCompleteListener {
                    imageProxy.close() // Always close the imageProxy
                }
        }
    }
}
