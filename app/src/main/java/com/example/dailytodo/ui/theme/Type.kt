package com.example.dailytodo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dailytodo.R

val Roboto = FontFamily(
    Font(R.font.roboto_thin),
    Font(R.font.roboto_medium),
    Font(R.font.roboto_regular),
    Font(R.font.roboto_bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    )
)