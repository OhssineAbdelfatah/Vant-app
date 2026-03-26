package com.vant.tracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vant.tracker.data.local.MediaItemEntity
import com.vant.tracker.data.repository.MediaRepository
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import kotlinx.coroutines.flow.*

data class StatusCount(val status: Status, val count: Int)
data class HomeUiState(
    val movieStatusCounts: List<StatusCount> = emptyList(),
    val tvStatusCounts: List<StatusCount> = emptyList(),
    val watchingMovies: List<MediaItemEntity> = emptyList(),
    val watchingTV: List<MediaItemEntity> = emptyList(),
)

class HomeViewModel(private val repo: MediaRepository) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        repo.getItemsByType(MediaType.MOVIE),
        repo.getItemsByType(MediaType.TV),
        repo.getItemsByTypeAndStatus(MediaType.MOVIE, Status.WATCHING),
        repo.getItemsByTypeAndStatus(MediaType.TV, Status.WATCHING),
    ) { movies, tvShows, watchingMovies, watchingTV ->
        HomeUiState(
            movieStatusCounts = Status.entries.map { s ->
                StatusCount(s, movies.count { it.status == s })
            },
            tvStatusCounts = Status.entries.map { s ->
                StatusCount(s, tvShows.count { it.status == s })
            },
            watchingMovies = watchingMovies,
            watchingTV = watchingTV,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}
