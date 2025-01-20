package com.example.auraprototype.ui.presentation.screen.splashScreen

import android.app.Instrumentation.ActivityResult
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun PermissionScreen(
    splashViewModel: SplashViewModel
){
    val context = LocalContext.current


    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){
        isGranted ->
            if (isGranted){

                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                splashViewModel.checkPermission(context = context)

            }else{
                //Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
    }
    // Display the UI with the request button
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Camera Permission is Required")
        Button(onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) }) {
            Text("Grant Permission")
        }
    }
}
