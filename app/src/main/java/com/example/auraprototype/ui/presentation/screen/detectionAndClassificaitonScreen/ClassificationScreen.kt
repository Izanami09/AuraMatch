package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.auraprototype.model.FilteredBread
import com.example.auraprototype.model.FilteredGlass
import com.example.auraprototype.model.FilteredHair
import com.example.auraprototype.model.Resource
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.theme.AccentColor
import com.example.auraprototype.ui.theme.BackgroundSecondary
import com.example.auraprototype.ui.theme.ErrorColor
import com.example.auraprototype.ui.theme.SurfaceLight
import com.example.auraprototype.ui.theme.TextPrimary
import com.example.auraprototype.ui.theme.TextSecondary
import com.example.auraprototype.ui.theme.TopAppBarColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassificationScreen(
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry")
    navBackStackEntry: NavBackStackEntry = remember {
        navController.getBackStackEntry("sharedGraph")
    }
) {
    val cameraViewModel: CameraViewModel = hiltViewModel(navBackStackEntry)
    val cameraUiState by cameraViewModel.cameraScreenState.collectAsState()
    val classificationUIState by cameraViewModel.classificationUIState.collectAsState()
    val capturedImage = remember { cameraUiState.image }
    val gender = cameraUiState.gender
    var searchText by remember { mutableStateOf("") }
    val faceShape by remember { derivedStateOf { cameraUiState.faceShape } }
    val scrollState = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var selectedItem by remember { mutableStateOf("home") }
    BackHandler {
        navController.navigate(AuraScreens.FaceDetectionScreen.route) {
            popUpTo(AuraScreens.ImageCropScreen.route) { inclusive = true } // Removes Screen B
        }
    }



    LaunchedEffect(faceShape) {
        if (!cameraUiState.isContentAlreadyFetched || cameraUiState.lastFetchedFaceShape != faceShape) {
            cameraViewModel.getRecommendationsForScreen(faceShape = faceShape, gender)
            cameraViewModel.contentFetched(true)
            cameraViewModel.updateLastFetchedFaceShape(faceShape)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(
                            text = "AuraMatch Advice",
                            style = MaterialTheme.typography.displayMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                },
                scrollBehavior = scrollState,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopAppBarColor
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(AuraScreens.FaceDetectionScreen.route) {
                            popUpTo(AuraScreens.ImageCropScreen.route) { inclusive = true } // Removes Screen B
                        } },
                        ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Arrow",
                            tint = Color.White
                        )
                    }
                }
            )
        },
//        bottomBar = {
//            BottomAppBar(
//                modifier = Modifier.fillMaxWidth()
//                    .height(50.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    modifier = Modifier.fillMaxWidth()
//                        .height(40.dp)
//                ) {
//                    IconButton(
//                        onClick = {
//                            if(navController.currentDestination?.route != AuraScreens.ClassificationScreen.route)
//                                navController.navigate(AuraScreens.ClassificationScreen.route)
//                                selectedItem = "profile"
//                        }
//                    ) {
//                        if(selectedItem == "profile" ) {
//                            Icon(
//                                Icons.Filled.Person,
//                                contentDescription = "Home"
//                            )
//                        }else{
//                            Icon(
//                                Icons.Outlined.Person,
//                                contentDescription = "Home"
//                            )
//                        }
//                    }
//                    IconButton(
//                        onClick = {
//                            if(navController.currentDestination?.route != AuraScreens.TryOnScreen.route)
//                                navController.navigate(AuraScreens.TryOnScreen.route)
//                            selectedItem = "home"
//                        }
//                    ) {
//                        if(selectedItem == "home" ) {
//                            Icon(
//                                Icons.Filled.Home,
//                                contentDescription = "Home"
//                            )
//                        }else{
//                            Icon(
//                                Icons.Outlined.Home,
//                                contentDescription = "Home"
//                            )
//                        }
//                    }
//                }
//            }
//        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceLight)
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
//                item {
//                    Image(
//                        bitmap = capturedImage!!.asImageBitmap(),
//                        contentDescription = "hello",
//                        contentScale = ContentScale.Inside
//                    )
//                }
                    // Face Shape Info
                    item {

                        Text(
                            text = "Your face shape is ${faceShape.replaceFirstChar {  it.uppercase() }}, and here are the styles we've picked just for you!",
                            color = TextPrimary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    when (classificationUIState) {
                        is Resource.Loading -> {
                            item { LoadingSection() }
                        }

                        is Resource.Success -> {
                            val success = (classificationUIState as Resource.Success).data

                            // Hair Styles Section
                            if (success.filteredHair.isNotEmpty()) {
                                item {
                                    CategorySection(
                                        title = "Hair Styles",
                                        items = success.filteredHair,
                                        onItemClick = { item ->
                                            cameraViewModel.onRecommendationClick(
                                                item,
                                                navController
                                            )
                                        }
                                    )
                                }
                            }

                            // Beard Styles Section (Only for male)
                            if (gender == "male" && success.filteredBread.isNotEmpty()) {
                                item {
                                    CategorySection(
                                        title = "Beard Styles",
                                        items = success.filteredBread,
                                        onItemClick = { item ->
                                            cameraViewModel.onRecommendationClick(
                                                item,
                                                navController
                                            )
                                        }
                                    )
                                }
                            }

                            // Glass Frames Section
                            if (success.filteredGlass.isNotEmpty()) {
                                item {
                                    CategorySection(
                                        title = "Glass Frames",
                                        items = success.filteredGlass,
                                        onItemClick = { item ->
                                            cameraViewModel.onRecommendationClick(
                                                item,
                                                navController
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        is Resource.Error -> {
                            item {
                                ErrorSection(
                                    message = (classificationUIState as Resource.Error).message,
                                    onRetry = {
                                        cameraViewModel.getRecommendationsForScreen(
                                            faceShape,
                                            gender
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun CategorySection(
    title: String,
    items: List<Any>,
    onItemClick: (Any) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = items,
                key = { item ->
                    when (item) {
                        is FilteredBread -> item.id!!
                        is FilteredHair -> item.id!!
                        is FilteredGlass -> item.id!!
                        else -> item.hashCode()
                    }
                }
            ) { item ->
                StyleCard(item = item, onClick = { onItemClick(item) })
            }
        }
    }
}

@Composable
private fun StyleCard(
    item: Any,
    onClick: () -> Unit
) {
    val faceShape =  when (item) {
        is FilteredBread -> item.faceShape
        is FilteredHair -> item.faceShape
        is FilteredGlass -> item.faceShape
        else -> null
    }
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(400.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundSecondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                val imageUrl = when (item) {
                    is FilteredBread -> item.image
                    is FilteredHair -> item.image
                    is FilteredGlass -> item.image
                    else -> null
                }

                AsyncImage(
                    model = imageUrl?.let {
                        "https://auramatch.onrender.com/${Uri.encode(it)}"
                    },
                    contentDescription = "Photos",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = when (item) {
                        is FilteredBread -> item.beardStyle ?: "Unknown Style"
                        is FilteredHair -> item.hairStyle ?: "Unknown Style"
                        is FilteredGlass -> item.glassStyle ?: "Unknown Style"
                        else -> "Unknown Style"
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                // Description text
                Text(
                    text = "Perfect for $faceShape shape",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = AccentColor
        )
    }
}

@Composable
private fun ErrorSection(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $message",
            color = ErrorColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03DAC5)
            )
            ) {
            Text("Retry")
        }
    }
}



