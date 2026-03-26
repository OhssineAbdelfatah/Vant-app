package com.vant.tracker.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vant.tracker.data.repository.MediaRepository
import com.vant.tracker.ui.theme.AppTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val THEME_KEY = stringPreferencesKey("app_theme")

data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val showClearDialog: Boolean = false,
    val clearSuccess: Boolean = false,
)

class SettingsViewModel(
    private val repo: MediaRepository,
    private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            context.dataStore.data.collect { prefs ->
                val themeStr = prefs[THEME_KEY] ?: AppTheme.SYSTEM.name
                _state.update { it.copy(theme = AppTheme.valueOf(themeStr)) }
            }
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            context.dataStore.edit { it[THEME_KEY] = theme.name }
        }
    }

    fun showClearDialog() = _state.update { it.copy(showClearDialog = true) }
    fun dismissClearDialog() = _state.update { it.copy(showClearDialog = false) }

    fun clearAllData() {
        viewModelScope.launch {
            repo.deleteAll()
            _state.update { it.copy(showClearDialog = false, clearSuccess = true) }
        }
    }

    fun clearSuccessAcknowledged() = _state.update { it.copy(clearSuccess = false) }
}
