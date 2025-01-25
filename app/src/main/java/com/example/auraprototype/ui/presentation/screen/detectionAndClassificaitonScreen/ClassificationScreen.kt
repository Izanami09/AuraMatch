package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.auraprototype.model.FilteredBread
import com.example.auraprototype.model.FilteredGlass
import com.example.auraprototype.model.FilteredHair
import com.example.auraprototype.model.Resource

@Composable
fun ClassificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry") navBackStackEntry: NavBackStackEntry = remember { navController.getBackStackEntry("sharedGraph") }
) {
    val cameraViewModel: CameraViewModel = hiltViewModel(navBackStackEntry)

    val cameraUiState by cameraViewModel.cameraScreenState.collectAsState()
    val classificationUIState by cameraViewModel.classificationUIState.collectAsState()


    val capturedImage = remember { cameraUiState.image }
    val faceShape = cameraUiState.faceShape
    val gender = cameraUiState.gender

    LaunchedEffect(Unit) {
        cameraViewModel.getRecommendationsForScreen(faceShape = faceShape, gender )
    }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                CapturedImageCard(capturedImage = capturedImage)
                Spacer(modifier = Modifier.height(16.dp))
                FaceShapeInfo(faceShape = faceShape)
                Spacer(modifier = Modifier.height(16.dp))
            }
            when (classificationUIState) {
                is Resource.Loading -> item {
                    CircularProgressIndicator(
                    )
                }

                is Resource.Success -> {
                    val success = (classificationUIState as Resource.Success).data

                    // Beard Recommendations
                    if (success.filteredBread != emptyList<FilteredBread>()) {
                        item { SectionTitle(title = "Beard Recommendations") }
                        item { RecommendationRow(items = success.filteredBread) }
                    }
                    // Hair Recommendations
                    item { SectionTitle(title = "Hair Recommendations") }
                    item { RecommendationRow(items = success.filteredHair) }

                    // Glass Recommendations
                    item { SectionTitle(title = "Glass Recommendations") }
                    item { RecommendationRow(items = success.filteredGlass) }
                }

                is Resource.Error -> {
                    item {
                        val errorMessage = (classificationUIState as Resource.Error).message
                        Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { cameraViewModel.getRecommendationsForScreen(faceShape, gender) }) {
                            Text(text = "Retry")
                        }
                    }
                }

                else ->{

                }
            }
        }

}


@Composable
fun RecommendationCard(filteredItem : Any) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(300.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
            ),
        shape = CardDefaults.elevatedShape

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
        ) {
            when (filteredItem) {
                is FilteredBread -> {
                    GridItems(imageUrl = filteredItem.image, imageName = filteredItem.beardStyle)

                }

                is FilteredHair -> {
                    GridItems(imageUrl = filteredItem.image, imageName = filteredItem.hairStyle)
                }

                is FilteredGlass -> {
                    GridItems(imageUrl = filteredItem.image, imageName = filteredItem.glassStyle)
                }

                else -> {
                    Text(text = "Error Loading Items")
                }

            }

        }
    }
}


@Composable
fun CapturedImageCard(capturedImage: Bitmap?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (capturedImage != null) {
                Image(
                    bitmap = capturedImage.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
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

@Composable
fun FaceShapeInfo(faceShape: String?) {
    Column {
        Text(
            text = "Your Face Shape: $faceShape",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Great Face Shape",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun RecommendationRow(items: List<Any>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = items, key = { it.hashCode() }) { item ->
            RecommendationCard(filteredItem = item)
        }
    }
}

@Composable
fun GridItems(
    imageUrl : String?,
    imageName : String?
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = "https://auramatch.onrender.com/${imageUrl}",
            contentDescription = "Beard Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .width(150.dp)
        )
        Text(
            text = imageName ?: "Name not available",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}


