package com.droidcon.simplejokes.core.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetSystemBarAppearance(darkTheme: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) { // Do not run in Composable previews
        val window = (view.context as? Activity)?.window
        LaunchedEffect(darkTheme, window) { // Re-run if theme or window changes
            window?.let {
                WindowInsetsControllerCompat(it, view).isAppearanceLightStatusBars = !darkTheme
                WindowInsetsControllerCompat(it, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
}