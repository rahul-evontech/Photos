package com.smartherd.photos.repository

import com.smartherd.photos.db.PhotoDatabase
import com.smartherd.photos.models.Hit
import com.smartherd.photos.models.Photos
import com.smartherd.photos.network.Api

class PhotosRepository(
    val api: Api,
    val db: PhotoDatabase
) {

    suspend fun getPhotos(pageNo: Int) = api.getPhotos(pageNo)

    suspend fun searchPhotos(search: String) = api.searchPhotos(search)

    suspend fun upsert(photos: Hit) = db.getDao().upsert(photos)

    fun getAllPhotos() = db.getDao().getAllPhotos()


}