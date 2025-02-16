package com.example.auraprototype.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Colors - used for buttons and highlights
val PrimaryColor =  Color(0xFF3D5AFE) // Deep Spotify green
val SecondaryColor = Color(0xFF5865F2) // Rich Discord blue
val AccentColor = Color(0xFFFF4081)    // Bold pink for accents

//TopAppBarColor
val TopAppBarColor =  Color(0xFF1F1F1F) // Slightly lighter gray

// Background Colors
val BackgroundPrimary = Color(0xFF121212)    // True black – perfect for OLED displays
val BackgroundSecondary = Color(0xFF1E1E1E)  // Dark gray – elevated surfaces
val BackgroundDark = Color(0xFF0A0A0A)       // Near black – ultra-dark mode

// Text Colors
val TextPrimary = Color(0xFFECECEC)          // Soft white – primary text
val TextSecondary = Color(0xFFB3B3B3)        // Light gray – secondary text
val TextTertiary = Color(0xFF8D8D8D)         // Muted gray – subtle text
val TextOnDark = Color(0xFFFFFFFF)           // Pure white – text on deep black backgrounds

// Status Colors
val SuccessColor =      Color(0xFF44F1A6)     // Vibrant emerald green – success states
val ErrorColor = Color(0xFFE74C3C)           // Striking crimson red – error states
val WarningColor = Color(0xFFF39C12)         // Golden yellow – warning states
val InfoColor = Color(0xFF3498DB)            // Bright azure blue – information states

// Gradient Colors
val PremiumGradient = listOf(
    Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364) // Deep blue-to-teal gradient
)

val successGradient = listOf(
    SuccessColor , Color(0xFFA4AFFF)
)

val AccentGradient = listOf(
    PrimaryColor,     // Start with deep green
    SecondaryColor    // End with rich Discord blue
)

// Surface Colors
val SurfaceLight = Color(0xFF1C1C1C)         // Dark gray – smooth surfaces
val SurfaceMedium = Color(0xFF252525)        // Slightly lighter dark gray – subtle dividers
val CardBorder = Color(0xFF333333)           // Soft dark gray for card borders

// Interactive States (darker shades of PrimaryColor)
val ButtonHover = Color(0xFF17A44B)          // Deeper green on hover
val ButtonPressed = Color(0xFF12803B)        // Even darker green for pressed state
