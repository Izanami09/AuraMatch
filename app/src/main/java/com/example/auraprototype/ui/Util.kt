package com.example.auraprototype.ui

import android.Manifest
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

//For Permission
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(permissionType : String, permission:String, onPermissionGranted : () -> Unit){
    val permissionState = rememberPermissionState(permission = permissionType)

    LaunchedEffect(permissionState.status){
        if (permissionState.status.isGranted){
            onPermissionGranted()
        }
    }

    when{
        permissionState.status.isGranted -> {

        }
        permissionState.status.shouldShowRationale -> {
            Text(text = "${permission} Permission is needed for this app to work.")
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text(text = "Please Grant The Required permission.")
            }
        }
        else -> {
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text(text = "Request Permission")
            }
        }
    }
}
