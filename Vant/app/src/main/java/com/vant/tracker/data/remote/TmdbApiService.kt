package com.vant.tracker.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// ── Response models ──────────────────────────────────────────────────────────

data class TmdbMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")    val posterPath: String?,
    @SerializedName("backdrop_path")  val backdropPath: String?,
    @SerializedName("release_date")   val releaseDate: String?,
    @SerializedName("vote_average")   val voteAverage: Float,
    val popularity: Float,
    @SerializedName("genre_ids")      val genreIds: List<Int> = emptyList(),
)

data class TmdbTrendingResponse(
    val page: Int,
    val results: List<TmdbMovie>,
    @SerializedName("total_pages")    val totalPages: Int,
    @SerializedName("total_results")  val totalResults: Int,
)

// ── Retrofit interface ───────────────────────────────────────────────────────

/**
 * Retrofit service interface for the TMDB v3 API.
 *
 * Base URL: `https://api.themoviedb.org/3/`
 *
 * All methods are `suspend` functions, intended for use inside coroutines or
 * a Kotlin Flow pipeline.
 */
interface TmdbApiService {

    /**
     * Fetch trending movies for the given time window.
     *
     * TMDB endpoint: `GET /trending/movie/{time_window}`
     *
     * @param timeWindow Either "day" or "week".
     * @param page       Result page number (default: 1).
     */
    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("page") page: Int = 1,
    ): TmdbTrendingResponse
}
