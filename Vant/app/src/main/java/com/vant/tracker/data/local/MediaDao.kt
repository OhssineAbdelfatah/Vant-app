package com.vant.tracker.data.local

import androidx.room.*
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Query("SELECT * FROM media_items ORDER BY updated_at DESC")
    fun getAllItems(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE id = :id")
    fun getItemById(id: Long): Flow<MediaItemEntity?>

    @Query("""
        SELECT * FROM media_items
        WHERE (:type IS NULL OR type = :type)
          AND (:status IS NULL OR status = :status)
          AND (:query = '' OR lower(title) LIKE '%' || lower(:query) || '%')
        ORDER BY
          CASE WHEN :sortBy = 'TITLE' THEN lower(title) END ASC,
          CASE WHEN :sortBy = 'RATING' THEN personal_rating END DESC,
          CASE WHEN :sortBy = 'UPDATED' THEN updated_at END DESC,
          updated_at DESC
    """)
    fun getFilteredItems(
        type: MediaType?,
        status: Status?,
        query: String,
        sortBy: String,
    ): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE type = :type AND status = :status ORDER BY updated_at DESC")
    fun getItemsByTypeAndStatus(type: MediaType, status: Status): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE type = :type ORDER BY updated_at DESC")
    fun getItemsByType(type: MediaType): Flow<List<MediaItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MediaItemEntity): Long

    @Update
    suspend fun update(item: MediaItemEntity)

    @Delete
    suspend fun delete(item: MediaItemEntity)

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM media_items")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM media_items WHERE type = :type AND status = :status")
    fun countByTypeAndStatus(type: MediaType, status: Status): Flow<Int>
}
