package com.vant.tracker.data.repository

import com.vant.tracker.data.local.MediaDao
import com.vant.tracker.data.local.MediaItemEntity
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import kotlinx.coroutines.flow.Flow

class MediaRepository(private val dao: MediaDao) {

    fun getAllItems(): Flow<List<MediaItemEntity>> = dao.getAllItems()

    fun getItemById(id: Long): Flow<MediaItemEntity?> = dao.getItemById(id)

    fun getFilteredItems(
        type: MediaType? = null,
        status: Status? = null,
        query: String = "",
        sortBy: String = "UPDATED",
    ): Flow<List<MediaItemEntity>> = dao.getFilteredItems(type, status, query, sortBy)

    fun getItemsByTypeAndStatus(type: MediaType, status: Status): Flow<List<MediaItemEntity>> =
        dao.getItemsByTypeAndStatus(type, status)

    fun getItemsByType(type: MediaType): Flow<List<MediaItemEntity>> =
        dao.getItemsByType(type)

    fun countByTypeAndStatus(type: MediaType, status: Status): Flow<Int> =
        dao.countByTypeAndStatus(type, status)

    suspend fun insert(item: MediaItemEntity): Long = dao.insert(item)

    suspend fun update(item: MediaItemEntity) = dao.update(item.copy(updatedAt = System.currentTimeMillis()))

    suspend fun delete(item: MediaItemEntity) = dao.delete(item)

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    suspend fun deleteAll() = dao.deleteAll()
}
