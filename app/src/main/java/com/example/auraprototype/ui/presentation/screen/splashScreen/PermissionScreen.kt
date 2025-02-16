package com.example.auraprototype.ui.presentation.screen.splashScreen


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.auraprototype.ui.theme.AccentGradient
import com.example.auraprototype.ui.theme.BackgroundPrimary
import com.example.auraprototype.ui.theme.BentosFontFamily
import com.example.auraprototype.ui.theme.TextOnDark
import com.example.auraprototype.ui.theme.TextPrimary
import com.example.auraprototype.ui.theme.TextSecondary

@Composable
fun PermissionScreen(
    splashViewModel: SplashViewModel
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            splashViewModel.checkPermission(context = context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundPrimary)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Camera Permission Required",
            style = MaterialTheme.typography.h6,
            color = TextPrimary,
            fontFamily = BentosFontFamily,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "To provide you with the best experience, we need access to your camera for style analysis.",
            style = MaterialTheme.typography.body1,
            color = TextSecondary,
            fontFamily = BentosFontFamily,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(colors = AccentGradient)
                )
                .clickable { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Grant Permission",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = BentosFontFamily,
                    color = TextOnDark
                )
            )
        }
    }
}