package com.example.auraprototype.ui.presentation.screen.classificationScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.auraprototype.model.FilteredBread
import com.example.auraprototype.model.FilteredGlass
import com.example.auraprototype.model.FilteredHair
import com.example.auraprototype.model.Resource
import com.example.auraprototype.ui.presentation.screen.cameraScreen.CameraViewModel

@Composable
fun ClassificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry") navBackStackEntry: NavBackStackEntry = remember { navController.getBackStackEntry("sharedGraph") }
) {
    val cameraViewModel: CameraViewModel = hiltViewModel(navBackStackEntry)

    val cameraUiState by cameraViewModel.cameraScreenState.collectAsState()
    val classificationUIState by cameraViewModel.classificationUIState.collectAsState()



    val capturedImage = cameraUiState.image
    val faceShape = cameraUiState.faceShape

    LaunchedEffect(Unit) {
        cameraViewModel.getRecommendationsForScreen(faceShape = faceShape)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Image Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (capturedImage != null) {
                        Image(
                            bitmap = (capturedImage).asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    } else {
                        Text(
                            text = "No Image Available",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Face Shape Information
            Text(
                text = "Your Face Shape: $faceShape",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Great Face Shape",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        when (classificationUIState) {
            is Resource.Loading -> {
                item {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val success = (classificationUIState as Resource.Success).data

                // Beard Recommendations
                item {
                    Text(
                        text = "Beard Recommendations",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        items(success.filteredBread) { beardRecommendation ->
                            RecommendationCard(filteredItem = beardRecommendation)
                        }
                    }
                }

                // Hair Recommendations
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Hair Recommendations",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        items(success.filteredHair) { filteredHair ->
                            RecommendationCard(filteredItem = filteredHair)
                        }
                    }
                }

                // Glass Recommendations
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Glass Recommendations",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        items(success.filteredGlass) { glassRecommendation ->
                            RecommendationCard(filteredItem = glassRecommendation)
                        }
                    }
                }
            }
            is Resource.Error -> {
                item {
                    val errorMessage = (classificationUIState as Resource.Error).message
                    Text(text = "Error: $errorMessage")
                }
            }
        }
    }
}


@Composable
fun RecommendationCard(filteredItem : Any) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when (filteredItem) {
                is FilteredBread -> {
                    Text(
                        text = filteredItem.beardStyle ?: "Name not available",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = filteredItem.description ?: "No description available.",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                is FilteredHair -> {
                    Text(
                        text = filteredItem.hairStyle ?: "Name not available",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = filteredItem.description ?: "No description available.",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                is FilteredGlass -> {
                    Text(
                        text = filteredItem.glassStyle ?: "No description available.",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = filteredItem.description ?: "No description available.",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                else -> {
                    Text(text = "Error Loading Items")
                }

            }

        }
    }
}


@Composable
fun ResizedImageView(image: Bitmap, faceShape : String) {
    val resizedImage = Bitmap.createScaledBitmap(image, 224, 224, true)
    Box(modifier = Modifier){
        Image(
            painter = BitmapPainter(resizedImage.asImageBitmap()),
            contentDescription = "Resized Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(text = faceShape)
    }

}

// Helper function to decode Base64 to Bitmap
fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64String.split(",")[1], Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



