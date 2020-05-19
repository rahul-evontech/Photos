package com.smartherd.photos.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smartherd.photos.models.Hit
import com.smartherd.photos.models.Photos

@Dao
interface PhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(photos: Hit)

    @Query("Select * from photos")
    fun getAllPhotos(): LiveData<List<Hit>>
}