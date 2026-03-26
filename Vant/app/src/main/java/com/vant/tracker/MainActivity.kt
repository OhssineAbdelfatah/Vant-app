package com.vant.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vant.tracker.ui.ViewModelFactory
import com.vant.tracker.ui.navigation.VantNavGraph
import com.vant.tracker.ui.settings.SettingsViewModel
import com.vant.tracker.ui.theme.VantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val factory = remember(context) { ViewModelFactory(context) }
            val settingsVm: SettingsViewModel = viewModel(factory = factory)
            val settingsState by settingsVm.state.collectAsState()

            VantTheme(appTheme = settingsState.theme) {
                VantNavGraph(
                    currentTheme = settingsState.theme,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
