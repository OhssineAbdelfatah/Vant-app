package com.vant.tracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// High-contrast Vant dark theme — pure-black backgrounds with vivid accent colours
// for maximum legibility on AMOLED/dark screens.
private val VantBackground   = Color(0xFF000000)
private val VantSurface      = Color(0xFF0D0D0D)
private val VantSurfaceVar   = Color(0xFF1A1A1A)

private val VantPrimary      = Color(0xFF00E5FF)   // Vivid cyan
private val VantOnPrimary    = Color(0xFF000000)
private val VantPrimaryContainer    = Color(0xFF004D5C)
private val VantOnPrimaryContainer  = Color(0xFFB3F6FF)

private val VantSecondary    = Color(0xFF69FFBA)   // Vivid mint-green
private val VantOnSecondary  = Color(0xFF000000)
private val VantSecondaryContainer    = Color(0xFF005235)
private val VantOnSecondaryContainer  = Color(0xFFB7FFE0)

private val VantTertiary     = Color(0xFFFFD740)   // Amber accent
private val VantOnTertiary   = Color(0xFF000000)
private val VantTertiaryContainer    = Color(0xFF4D3A00)
private val VantOnTertiaryContainer  = Color(0xFFFFEFB5)

private val VantError        = Color(0xFFFF5252)
private val VantOnError      = Color(0xFF000000)
private val VantErrorContainer    = Color(0xFF5C0000)
private val VantOnErrorContainer  = Color(0xFFFFB4AB)

private val VantOnBackground = Color(0xFFF5F5F5)
private val VantOnSurface    = Color(0xFFEEEEEE)
private val VantOnSurfaceVar = Color(0xFFCCCCCC)

private val VantOutline      = Color(0xFF555555)
private val VantOutlineVar   = Color(0xFF333333)

private val VantDarkColorScheme = darkColorScheme(
    primary                = VantPrimary,
    onPrimary              = VantOnPrimary,
    primaryContainer       = VantPrimaryContainer,
    onPrimaryContainer     = VantOnPrimaryContainer,
    secondary              = VantSecondary,
    onSecondary            = VantOnSecondary,
    secondaryContainer     = VantSecondaryContainer,
    onSecondaryContainer   = VantOnSecondaryContainer,
    tertiary               = VantTertiary,
    onTertiary             = VantOnTertiary,
    tertiaryContainer      = VantTertiaryContainer,
    onTertiaryContainer    = VantOnTertiaryContainer,
    error                  = VantError,
    onError                = VantOnError,
    errorContainer         = VantErrorContainer,
    onErrorContainer       = VantOnErrorContainer,
    background             = VantBackground,
    onBackground           = VantOnBackground,
    surface                = VantSurface,
    onSurface              = VantOnSurface,
    surfaceVariant         = VantSurfaceVar,
    onSurfaceVariant       = VantOnSurfaceVar,
    outline                = VantOutline,
    outlineVariant         = VantOutlineVar,
)

/**
 * High-contrast, dark-mode-first theme for the Vant media tracking app.
 *
 * Always applies the dark colour scheme — Vant is intentionally dark-only.
 */
@Composable
fun VantTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VantDarkColorScheme,
        content = content,
    )
}
