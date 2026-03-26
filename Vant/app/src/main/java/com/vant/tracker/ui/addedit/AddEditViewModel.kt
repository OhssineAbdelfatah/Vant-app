package com.vant.tracker.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vant.tracker.data.local.MediaItemEntity
import com.vant.tracker.data.repository.MediaRepository
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AddEditUiState(
    val id: Long = 0,
    val createdAt: Long = 0L,
    val title: String = "",
    val type: MediaType = MediaType.MOVIE,
    val year: String = "",
    val status: Status = Status.PLANNED,
    val personalRating: String = "",
    val notes: String = "",
    val seasons: String = "",
    val episodesTotal: String = "",
    val lastWatchedSeason: String = "",
    val lastWatchedEpisode: String = "",
    val titleError: String? = null,
    val ratingError: String? = null,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
)

class AddEditViewModel(private val repo: MediaRepository) : ViewModel() {

    private val _state = MutableStateFlow(AddEditUiState())
    val state: StateFlow<AddEditUiState> = _state.asStateFlow()

    fun loadItem(id: Long) {
        viewModelScope.launch {
            repo.getItemById(id).firstOrNull()?.let { item ->
                _state.update {
                    it.copy(
                        id = item.id,
                        createdAt = item.createdAt,
                        title = item.title,
                        type = item.type,
                        year = item.year?.toString() ?: "",
                        status = item.status,
                        personalRating = item.personalRating?.toString() ?: "",
                        notes = item.notes,
                        seasons = item.seasons?.toString() ?: "",
                        episodesTotal = item.episodesTotal?.toString() ?: "",
                        lastWatchedSeason = item.lastWatchedSeason?.toString() ?: "",
                        lastWatchedEpisode = item.lastWatchedEpisode?.toString() ?: "",
                    )
                }
            }
        }
    }

    fun setTitle(v: String) = _state.update { it.copy(title = v, titleError = null) }
    fun setType(v: MediaType) = _state.update { it.copy(type = v) }
    fun setYear(v: String) = _state.update { it.copy(year = v) }
    fun setStatus(v: Status) = _state.update { it.copy(status = v) }
    fun setRating(v: String) = _state.update { it.copy(personalRating = v, ratingError = null) }
    fun setNotes(v: String) = _state.update { it.copy(notes = v) }
    fun setSeasons(v: String) = _state.update { it.copy(seasons = v) }
    fun setEpisodesTotal(v: String) = _state.update { it.copy(episodesTotal = v) }
    fun setLastWatchedSeason(v: String) = _state.update { it.copy(lastWatchedSeason = v) }
    fun setLastWatchedEpisode(v: String) = _state.update { it.copy(lastWatchedEpisode = v) }

    fun save() {
        val s = _state.value
        var hasError = false
        if (s.title.isBlank()) {
            _state.update { it.copy(titleError = "Title is required") }
            hasError = true
        }
        val rating = s.personalRating.toIntOrNull()
        if (s.personalRating.isNotEmpty() && (rating == null || rating < 1 || rating > 10)) {
            _state.update { it.copy(ratingError = "Rating must be 1–10") }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val entity = MediaItemEntity(
                id = s.id,
                title = s.title.trim(),
                type = s.type,
                year = s.year.toIntOrNull(),
                status = s.status,
                personalRating = s.personalRating.toIntOrNull(),
                notes = s.notes.trim(),
                seasons = s.seasons.toIntOrNull(),
                episodesTotal = s.episodesTotal.toIntOrNull(),
                lastWatchedSeason = s.lastWatchedSeason.toIntOrNull(),
                lastWatchedEpisode = s.lastWatchedEpisode.toIntOrNull(),
                createdAt = if (s.id == 0L) now else s.createdAt,
                updatedAt = now,
            )
            if (s.id == 0L) {
                repo.insert(entity)
            } else {
                repo.update(entity)
            }
            _state.update { it.copy(isSaved = true) }
        }
    }
}
