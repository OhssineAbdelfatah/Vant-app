package com.vant.tracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing a tracked media item (movie or TV show).
 *
 * For movies, the season/episode fields remain at their default values (0).
 * For TV shows, [currentSeason] and [currentEpisode] track viewing progress
 * while [totalSeasons] and [totalEpisodes] record the full run of the series.
 */
@Entity(
    tableName = "media_items",
    indices = [Index(value = ["tmdb_id", "media_type"], unique = true)],
)
data class MediaItem(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** TMDB identifier — combined with [mediaType] forms a unique key. */
    @ColumnInfo(name = "tmdb_id")
    val tmdbId: Int,

    /** Human-readable title (movie title or series name). */
    val title: String,

    /** Either "movie" or "tv". */
    @ColumnInfo(name = "media_type")
    val mediaType: String,

    /** Relative poster image path as returned by TMDB (e.g. "/abc123.jpg"). */
    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    /** Relative backdrop image path as returned by TMDB. */
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String? = null,

    /** Short plot synopsis. */
    val overview: String? = null,

    /** ISO 8601 release / first-air date string (e.g. "2024-03-15"). */
    @ColumnInfo(name = "release_date")
    val releaseDate: String? = null,

    /** TMDB audience score (0–10). */
    @ColumnInfo(name = "vote_average")
    val voteAverage: Float = 0f,

    // ── Season / episode progress (TV shows only) ──────────────────────────

    /** Season the user is currently watching (1-based; 0 = not started). */
    @ColumnInfo(name = "current_season")
    val currentSeason: Int = 0,

    /** Episode within [currentSeason] the user is currently on (1-based; 0 = not started). */
    @ColumnInfo(name = "current_episode")
    val currentEpisode: Int = 0,

    /** Total number of seasons in the series (0 for movies). */
    @ColumnInfo(name = "total_seasons")
    val totalSeasons: Int = 0,

    /** Total number of episodes across all seasons (0 for movies). */
    @ColumnInfo(name = "total_episodes")
    val totalEpisodes: Int = 0,

    // ── Watch status ────────────────────────────────────────────────────────

    /** True once the user has finished the movie or watched the series finale. */
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    // ── Timestamps (epoch millis) ───────────────────────────────────────────

    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
)
