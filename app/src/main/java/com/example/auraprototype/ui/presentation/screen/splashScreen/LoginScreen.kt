package com.example.auraprototype.ui.presentation.screen.splashScreen

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.auraprototype.R
import com.example.auraprototype.ui.navigation.AuraScreens
import com.example.auraprototype.ui.theme.BackgroundPrimary
import com.example.auraprototype.ui.theme.BackgroundSecondary
import com.example.auraprototype.ui.theme.BentosFontFamily

import com.example.auraprototype.ui.theme.PremiumGradient
import com.example.auraprototype.ui.theme.TextOnDark
import com.example.auraprototype.ui.theme.TextPrimary
import com.example.auraprototype.ui.theme.TextTertiary


@Composable
fun LoginScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(BackgroundPrimary),  // Updated to use our theme color
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .height(530.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.aura_login_screen_head_top),
                contentDescription = "Aura Match Landing Page",
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 0.dp, 24.dp, 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Unlock new possibilities",
                style = MaterialTheme.typography.h5,
                fontFamily = BentosFontFamily,
                color = TextPrimary,  // Updated to theme color
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Join thousands of people who are enhancing their style and confidence with Aura Match.",
                style = MaterialTheme.typography.body1,
                fontFamily = BentosFontFamily,
                color = TextTertiary,  // Updated for better readability
                textAlign = TextAlign.Center
            )
        }
        GradientButton(onClick = { navController.navigate(AuraScreens.GenderScreen.route)}, "Get Started" )
    }
}

@Composable
fun GradientButton(onClick: () -> Unit, message : String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .height(63.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = PremiumGradient  // Updated to premium gradient
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontFamily = BentosFontFamily,
                color = TextOnDark  // Updated for better contrast
            )
        )
    }
}