package com.neuroverse.auth.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Indigo Palette
val PrimaryLight = Color(0xFF4F46E5)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFE0E7FF)
val OnPrimaryContainerLight = Color(0xFF1E1B4B)

// Secondary Sky Palette
val SecondaryLight = Color(0xFF0EA5E9)
val OnSecondaryLight = Color(0xFFFFFFFF)

// Surfaces & Backgrounds - Light
val BackgroundLight = Color(0xFFF8FAFC)
val OnBackgroundLight = Color(0xFF0F172A)
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF0F172A)
val SurfaceVariantLight = Color(0xFFF1F5F9)
val OnSurfaceVariantLight = Color(0xFF64748B)
val OutlineLight = Color(0xFFCBD5E1)

// Feedback & Errors - Light
val ErrorLight = Color(0xFFDC2626)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFEE2E2)
val OnErrorContainerLight = Color(0xFF7F1D1D)

// Dark Palette
val PrimaryDark = Color(0xFF818CF8)
val OnPrimaryDark = Color(0xFF1E1B4B)
val PrimaryContainerDark = Color(0xFF3730A3)
val OnPrimaryContainerDark = Color(0xFFE0E7FF)

val SecondaryDark = Color(0xFF38BDF8)
val OnSecondaryDark = Color(0xFF0C4A6E)

val BackgroundDark = Color(0xFF0B1020)
val OnBackgroundDark = Color(0xFFF1F5F9)
val SurfaceDark = Color(0xFF131A2E)
val OnSurfaceDark = Color(0xFFF1F5F9)
val SurfaceVariantDark = Color(0xFF1E293B)
val OnSurfaceVariantDark = Color(0xFF94A3B8)
val OutlineDark = Color(0xFF334155)

val ErrorDark = Color(0xFFF87171)
val OnErrorDark = Color(0xFF450A0A)
val ErrorContainerDark = Color(0xFF7F1D1D)
val OnErrorContainerDark = Color(0xFFFEE2E2)

// Extended Feedback
val SuccessColor = Color(0xFF16A34A)
val SuccessContainer = Color(0xFFDCFCE7)
val OnSuccessContainer = Color(0xFF14532D)

// Brand Gradient: #4F46E5 to #7C3AED at 135 degrees
val BrandGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF4F46E5),
        Color(0xFF7C3AED)
    )
)
