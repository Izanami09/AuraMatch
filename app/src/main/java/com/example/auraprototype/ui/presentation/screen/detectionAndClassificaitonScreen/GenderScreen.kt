package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.auraprototype.R
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.theme.BackgroundPrimary
import com.example.auraprototype.ui.theme.BentosFontFamily
import com.example.auraprototype.ui.theme.PremiumGradient


import com.example.auraprototype.ui.theme.PrimaryColor
import com.example.auraprototype.ui.theme.SuccessColor
import com.example.auraprototype.ui.theme.TextPrimary
import com.example.auraprototype.ui.theme.TextSecondary
import com.example.auraprototype.ui.theme.TopAppBarColor
import com.example.auraprototype.ui.theme.successGradient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderScreen(
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry") navBackStackEntry: NavBackStackEntry = remember {
        navController.getBackStackEntry("sharedGraph")
    }
) {
    val cameraViewModel: CameraViewModel = hiltViewModel(navBackStackEntry)

    val genderUiState by cameraViewModel.cameraScreenState.collectAsState()

    var gender = genderUiState.gender
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                        Text(
                            text = "Select Gender",
                            style = MaterialTheme.typography.headlineMedium,
                            fontStyle = FontStyle.Normal,
                            fontFamily = BentosFontFamily,
                            color = Color.White

                        )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopAppBarColor
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {navController.popBackStack()},

                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Arrow",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(BackgroundPrimary)
                .padding(it)
                .padding(20.dp),

            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            androidx.compose.material.Text(
                text = "Please select your gender to proceed further.",
                style = androidx.compose.material.MaterialTheme.typography.body1,
                fontFamily = BentosFontFamily,
                color = Color(0xFFA1A4B2),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                            cameraViewModel.maleButtonClicked()
                          },
                shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                border = if(gender == "male") { BorderStroke(5.dp, Brush.linearGradient(colors = successGradient)) } else {ButtonDefaults.outlinedButtonBorder.copy(5.dp, Brush.linearGradient(colors = PremiumGradient))}
            ) {
                GenderImage(image = R.drawable.male)
            }
            Button(
                onClick = {
                            cameraViewModel.femaleButtonClicked()
                          },
                shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                border = if(gender == "female") { BorderStroke(5.dp, Brush.linearGradient(colors = successGradient)) } else {ButtonDefaults.outlinedButtonBorder.copy(5.dp, Brush.linearGradient(colors = PremiumGradient))}
            ) {
                GenderImage(image = R.drawable.female)
            }

                Button(
                    onClick = { navController.navigate(AuraScreens.FaceDetectionScreen.route) },
                    enabled = (genderUiState.gender != ""),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessColor,
                        contentColor = Color(0xFAF6F1FB),
                        disabledContainerColor = Color(0xFF8E97AA),
                        disabledContentColor = Color(0xFAF6F1FB)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                        .height(63.dp)
                ) {
                        Text(
                            text = "Continue",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontFamily = BentosFontFamily,
                                color = Color(0xFFF6F1FB)
                            )
                        )
                }
            }
        }
}

@Composable
fun GenderImage(image : Int){
    Card(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape),
        colors = CardDefaults.cardColors(Color(0xFFE1E1E5)), // Background color
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Male",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
            )
    }

}





