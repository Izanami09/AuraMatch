package com.example.auraprototype.ui.theme

import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.auraprototype.R

val BentosFontFamily = FontFamily(
    Font(R.font.bentos_sans_bold, FontWeight.Bold, FontStyle.Normal) ,
    Font(R.font.bentos_san_book, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.bentos_sans_black, FontWeight.Bold, FontStyle.Normal)
)


val MyTypography = Typography(
    // Display styles
    displayLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),
    displayMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),
    displaySmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),

    // Headline styles
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),

    // Title styles
    titleLarge = TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),
    titleMedium = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),
    titleSmall = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),

    // Body styles
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = BentosFontFamily
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = BentosFontFamily
    ),
    bodySmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Thin,
        fontFamily = BentosFontFamily,
        color = Color.Black
    ),

    // Label styles
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = BentosFontFamily,
        color = Color.Gray
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = BentosFontFamily,
        color = Color.Gray
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = BentosFontFamily,
        color = Color.Gray
    )

)

