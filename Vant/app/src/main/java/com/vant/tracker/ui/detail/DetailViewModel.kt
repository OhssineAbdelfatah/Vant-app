package com.vant.tracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vant.tracker.data.local.MediaItemEntity
import com.vant.tracker.data.repository.MediaRepository
import com.vant.tracker.domain.model.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DetailUiState(
    val item: MediaItemEntity? = null,
    val isDeleted: Boolean = false,
    val isLoading: Boolean = true,
)

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel(private val repo: MediaRepository) : ViewModel() {

    private val _itemId = MutableStateFlow<Long?>(null)
    private val _isDeleted = MutableStateFlow(false)

    val uiState: StateFlow<DetailUiState> = combine(
        _itemId.filterNotNull().flatMapLatest { id -> repo.getItemById(id) },
        _isDeleted,
    ) { item, deleted ->
        DetailUiState(item = item, isDeleted = deleted, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailUiState())

    fun load(id: Long) { _itemId.value = id }

    fun updateStatus(status: Status) {
        viewModelScope.launch {
            uiState.value.item?.let { repo.update(it.copy(status = status)) }
        }
    }

    fun updateRating(rating: Int?) {
        viewModelScope.launch {
            uiState.value.item?.let { repo.update(it.copy(personalRating = rating)) }
        }
    }

    fun updateNotes(notes: String) {
        viewModelScope.launch {
            uiState.value.item?.let { repo.update(it.copy(notes = notes)) }
        }
    }

    fun updateLastWatched(season: Int?, episode: Int?) {
        viewModelScope.launch {
            uiState.value.item?.let {
                repo.update(it.copy(lastWatchedSeason = season, lastWatchedEpisode = episode))
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            uiState.value.item?.let {
                repo.delete(it)
                _isDeleted.value = true
            }
        }
    }
}
