package com.example.auraprototype.ui.presentation.screen.genderScreen

import android.annotation.SuppressLint
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.auraprototype.R
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.presentation.screen.cameraScreen.CameraViewModel

@Composable
fun GenderScreen(
    modifier : Modifier = Modifier.fillMaxSize(),
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry") navBackStackEntry: NavBackStackEntry = remember {
        navController.getBackStackEntry("sharedGraph")
    }
){
    val cameraViewModel : CameraViewModel = hiltViewModel(navBackStackEntry)

    val genderUiState by cameraViewModel.cameraScreenState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(
            onClick = { cameraViewModel.maleButtonClicked() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_male),
                contentDescription = "Male",
            )
        }
        Button(onClick = { cameraViewModel.femaleButtonClicked() }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_female),
                contentDescription = "Male",
            )
        }

        if (genderUiState.gender != "") {
            Button(onClick = { navController.navigate(AuraScreens.FaceDetectionScreen.route)}) {
                Text(text = "Continue")
            }
        }
    }
}


