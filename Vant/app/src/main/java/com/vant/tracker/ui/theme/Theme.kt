package com.vant.tracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Dark palette ─────────────────────────────────────────────────────────────
private val DarkBackground   = Color(0xFF000000)
private val DarkSurface      = Color(0xFF0D0D0D)
private val DarkSurfaceVar   = Color(0xFF1A1A1A)
private val DarkPrimary      = Color(0xFF00E5FF)
private val DarkOnPrimary    = Color(0xFF000000)
private val DarkPrimaryContainer    = Color(0xFF004D5C)
private val DarkOnPrimaryContainer  = Color(0xFFB3F6FF)
private val DarkSecondary    = Color(0xFF69FFBA)
private val DarkOnSecondary  = Color(0xFF000000)
private val DarkSecondaryContainer    = Color(0xFF005235)
private val DarkOnSecondaryContainer  = Color(0xFFB7FFE0)
private val DarkTertiary     = Color(0xFFFFD740)
private val DarkOnTertiary   = Color(0xFF000000)
private val DarkTertiaryContainer    = Color(0xFF4D3A00)
private val DarkOnTertiaryContainer  = Color(0xFFFFEFB5)
private val DarkError        = Color(0xFFFF5252)
private val DarkOnError      = Color(0xFF000000)
private val DarkErrorContainer    = Color(0xFF5C0000)
private val DarkOnErrorContainer  = Color(0xFFFFB4AB)
private val DarkOnBackground = Color(0xFFF5F5F5)
private val DarkOnSurface    = Color(0xFFEEEEEE)
private val DarkOnSurfaceVar = Color(0xFFCCCCCC)
private val DarkOutline      = Color(0xFF555555)
private val DarkOutlineVar   = Color(0xFF333333)

private val VantDarkColorScheme = darkColorScheme(
    primary                = DarkPrimary,
    onPrimary              = DarkOnPrimary,
    primaryContainer       = DarkPrimaryContainer,
    onPrimaryContainer     = DarkOnPrimaryContainer,
    secondary              = DarkSecondary,
    onSecondary            = DarkOnSecondary,
    secondaryContainer     = DarkSecondaryContainer,
    onSecondaryContainer   = DarkOnSecondaryContainer,
    tertiary               = DarkTertiary,
    onTertiary             = DarkOnTertiary,
    tertiaryContainer      = DarkTertiaryContainer,
    onTertiaryContainer    = DarkOnTertiaryContainer,
    error                  = DarkError,
    onError                = DarkOnError,
    errorContainer         = DarkErrorContainer,
    onErrorContainer       = DarkOnErrorContainer,
    background             = DarkBackground,
    onBackground           = DarkOnBackground,
    surface                = DarkSurface,
    onSurface              = DarkOnSurface,
    surfaceVariant         = DarkSurfaceVar,
    onSurfaceVariant       = DarkOnSurfaceVar,
    outline                = DarkOutline,
    outlineVariant         = DarkOutlineVar,
)

// ── Light palette ─────────────────────────────────────────────────────────────
private val VantLightColorScheme = lightColorScheme(
    primary                = Color(0xFF006878),
    onPrimary              = Color(0xFFFFFFFF),
    primaryContainer       = Color(0xFFB3F6FF),
    onPrimaryContainer     = Color(0xFF001F26),
    secondary              = Color(0xFF006C47),
    onSecondary            = Color(0xFFFFFFFF),
    secondaryContainer     = Color(0xFFB7FFE0),
    onSecondaryContainer   = Color(0xFF002113),
    tertiary               = Color(0xFF6B5E00),
    onTertiary             = Color(0xFFFFFFFF),
    tertiaryContainer      = Color(0xFFFFEFB5),
    onTertiaryContainer    = Color(0xFF211B00),
    error                  = Color(0xFFBA1A1A),
    onError                = Color(0xFFFFFFFF),
    errorContainer         = Color(0xFFFFDAD6),
    onErrorContainer       = Color(0xFF410002),
    background             = Color(0xFFF8FDFF),
    onBackground           = Color(0xFF001F26),
    surface                = Color(0xFFF8FDFF),
    onSurface              = Color(0xFF001F26),
    surfaceVariant         = Color(0xFFDBE4E7),
    onSurfaceVariant       = Color(0xFF3F484B),
    outline                = Color(0xFF6F797B),
    outlineVariant         = Color(0xFFBFC8CB),
)

enum class AppTheme { SYSTEM, LIGHT, DARK }

@Composable
fun VantTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (appTheme) {
        AppTheme.DARK   -> true
        AppTheme.LIGHT  -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (darkTheme) VantDarkColorScheme else VantLightColorScheme,
        content = content,
    )
}
