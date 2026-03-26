package com.vant.tracker.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vant.tracker.data.local.MediaItemEntity
import com.vant.tracker.data.repository.MediaRepository
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LibraryFilter(
    val query: String = "",
    val type: MediaType? = null,
    val status: Status? = null,
    val sortBy: String = "UPDATED",
)

data class LibraryUiState(
    val items: List<MediaItemEntity> = emptyList(),
    val filter: LibraryFilter = LibraryFilter(),
    val isLoading: Boolean = true,
)

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModel(private val repo: MediaRepository) : ViewModel() {

    private val _filter = MutableStateFlow(LibraryFilter())

    val uiState: StateFlow<LibraryUiState> = _filter
        .flatMapLatest { f ->
            repo.getFilteredItems(f.type, f.status, f.query, f.sortBy)
                .map { items -> LibraryUiState(items = items, filter = f, isLoading = false) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LibraryUiState())

    fun setQuery(query: String) { _filter.update { it.copy(query = query) } }
    fun setTypeFilter(type: MediaType?) { _filter.update { it.copy(type = type) } }
    fun setStatusFilter(status: Status?) { _filter.update { it.copy(status = status) } }
    fun setSortBy(sortBy: String) { _filter.update { it.copy(sortBy = sortBy) } }

    fun deleteItem(item: MediaItemEntity) {
        viewModelScope.launch { repo.delete(item) }
    }

    fun markCompleted(item: MediaItemEntity) {
        viewModelScope.launch { repo.update(item.copy(status = Status.COMPLETED)) }
    }
}
