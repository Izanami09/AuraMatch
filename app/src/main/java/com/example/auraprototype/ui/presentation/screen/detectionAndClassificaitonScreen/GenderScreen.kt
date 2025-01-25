package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.auraprototype.R
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.theme.NeutralGrey
import com.example.auraprototype.ui.theme.PrimaryColor
import com.example.auraprototype.ui.theme.SuccessColor
import com.example.auraprototype.ui.theme.colorStops

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderScreen(
    modifier : Modifier = Modifier.fillMaxSize(),
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
                            style = MaterialTheme.typography.titleLarge,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.Monospace

                        )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryColor
                )
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Button(
                onClick = {
                            cameraViewModel.maleButtonClicked()
                          },
                shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                border = if(gender == "male") { BorderStroke(2.dp, SuccessColor) } else {ButtonDefaults.outlinedButtonBorder}
            ) {
                GenderImage(image = R.drawable.male)
            }
            Button(
                onClick = {
                            cameraViewModel.femaleButtonClicked()
                          },
                shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                border = if(gender == "female") { BorderStroke(2.dp, SuccessColor) } else {ButtonDefaults.outlinedButtonBorder}
            ) {
                GenderImage(image = R.drawable.female)
            }

                Button(
                    onClick = { navController.navigate(AuraScreens.FaceDetectionScreen.route) },
                    enabled = (genderUiState.gender != ""),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(50.dp)
                        .width(200.dp)
                ) {
                        Text(
                            text = "Continue"
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
        colors = CardDefaults.cardColors(NeutralGrey), // Background color
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


@Preview(showBackground = true)
@Composable
fun abc(){
    GenderImage(image = R.drawable.female)
}

