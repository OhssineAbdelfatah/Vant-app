package com.vant.tracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status

@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val type: MediaType,
    val title: String,
    val year: Int? = null,

    @ColumnInfo(name = "poster_uri")
    val posterUri: String? = null,

    val status: Status = Status.PLANNED,

    @ColumnInfo(name = "personal_rating")
    val personalRating: Int? = null,

    val notes: String = "",

    // TV-only
    val seasons: Int? = null,

    @ColumnInfo(name = "episodes_total")
    val episodesTotal: Int? = null,

    @ColumnInfo(name = "last_watched_season")
    val lastWatchedSeason: Int? = null,

    @ColumnInfo(name = "last_watched_episode")
    val lastWatchedEpisode: Int? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
)
