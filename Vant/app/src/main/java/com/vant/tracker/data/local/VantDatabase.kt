package com.vant.tracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromMediaType(value: MediaType): String = value.name
    @TypeConverter
    fun toMediaType(value: String): MediaType = MediaType.valueOf(value)
    @TypeConverter
    fun fromStatus(value: Status): String = value.name
    @TypeConverter
    fun toStatus(value: String): Status = Status.valueOf(value)
}

@Database(entities = [MediaItemEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class VantDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao

    companion object {
        @Volatile private var INSTANCE: VantDatabase? = null

        fun getInstance(context: Context): VantDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(context, VantDatabase::class.java, "vant.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            seedData(getInstance(context).mediaDao())
                        }
                    }
                })
                .build()

        private suspend fun seedData(dao: MediaDao) {
            val now = System.currentTimeMillis()
            listOf(
                MediaItemEntity(type = MediaType.MOVIE, title = "Inception", year = 2010, status = Status.COMPLETED, personalRating = 9, notes = "Mind-bending thriller", createdAt = now, updatedAt = now),
                MediaItemEntity(type = MediaType.MOVIE, title = "Dune: Part Two", year = 2024, status = Status.WATCHING, personalRating = 8, notes = "", createdAt = now, updatedAt = now),
                MediaItemEntity(type = MediaType.MOVIE, title = "The Batman", year = 2022, status = Status.PLANNED, personalRating = null, notes = "", createdAt = now, updatedAt = now),
                MediaItemEntity(type = MediaType.TV, title = "Breaking Bad", year = 2008, status = Status.COMPLETED, personalRating = 10, notes = "Best TV show ever", seasons = 5, episodesTotal = 62, lastWatchedSeason = 5, lastWatchedEpisode = 16, createdAt = now, updatedAt = now),
                MediaItemEntity(type = MediaType.TV, title = "The Bear", year = 2022, status = Status.WATCHING, personalRating = 9, notes = "Intense kitchen drama", seasons = 3, episodesTotal = 28, lastWatchedSeason = 2, lastWatchedEpisode = 5, createdAt = now, updatedAt = now),
                MediaItemEntity(type = MediaType.TV, title = "Shogun", year = 2024, status = Status.PLANNED, personalRating = null, notes = "", seasons = 1, episodesTotal = 10, createdAt = now, updatedAt = now),
            ).forEach { dao.insert(it) }
        }
    }
}
