package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CelestialLightColorScheme =
  lightColorScheme(
    primary = CelestialPrimaryBright,
    onPrimary = CelestialOnPrimary,
    primaryContainer = CelestialPrimaryContainer,
    onPrimaryContainer = CelestialOnPrimaryContainer,
    secondary = CelestialSecondary,
    onSecondary = CelestialOnPrimary,
    secondaryContainer = CelestialSecondaryContainer,
    onSecondaryContainer = CelestialOnSecondaryContainer,
    tertiary = CelestialTertiary,
    onTertiary = CelestialOnPrimary,
    tertiaryContainer = CelestialTertiaryContainer,
    background = CelestialBackground,
    onBackground = CelestialOnSurface,
    surface = CelestialSurface,
    onSurface = CelestialOnSurface,
    surfaceVariant = CelestialSurfaceVariant,
    onSurfaceVariant = CelestialOnSurfaceVariant,
    outline = CelestialOutline,
    outlineVariant = CelestialOutlineVariant
  )

private val CelestialDarkColorScheme =
  darkColorScheme(
    primary = CelestialSky,
    onPrimary = CelestialSlateDeep,
    primaryContainer = CelestialPrimary,
    onPrimaryContainer = CelestialOnPrimary,
    secondary = CelestialSecondaryContainer,
    onSecondary = CelestialSlateDeep,
    background = CelestialSlateDeep,
    onBackground = CelestialBackground,
    surface = CelestialSlateMedium,
    onSurface = CelestialBackground,
    surfaceVariant = CelestialSlateDeep,
    onSurfaceVariant = CelestialBorderHairline
  )

@Composable
fun NoorTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) CelestialDarkColorScheme else CelestialLightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  NoorTheme(darkTheme = darkTheme, content = content)
}
