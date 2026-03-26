package com.vant.tracker.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vant.tracker.data.local.VantDatabase
import com.vant.tracker.data.repository.MediaRepository
import com.vant.tracker.ui.addedit.AddEditViewModel
import com.vant.tracker.ui.detail.DetailViewModel
import com.vant.tracker.ui.home.HomeViewModel
import com.vant.tracker.ui.library.LibraryViewModel
import com.vant.tracker.ui.settings.SettingsViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val repo: MediaRepository by lazy {
        MediaRepository(VantDatabase.getInstance(context).mediaDao())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java)     -> HomeViewModel(repo) as T
        modelClass.isAssignableFrom(LibraryViewModel::class.java)  -> LibraryViewModel(repo) as T
        modelClass.isAssignableFrom(AddEditViewModel::class.java)  -> AddEditViewModel(repo) as T
        modelClass.isAssignableFrom(DetailViewModel::class.java)   -> DetailViewModel(repo) as T
        modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(repo, context) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
