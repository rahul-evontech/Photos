package com.smartherd.photos.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "photos"
)
data class Hit(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("pageURL")
    val pageURL: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("tags")
    val tags: String = "",
    @SerializedName("previewURL")
    val previewURL: String = "",
    @SerializedName("previewWidth")
    val previewWidth: Int = 0,
    @SerializedName("previewHeight")
    val previewHeight: Int = 0,
    @SerializedName("webformatURL")
    val webformatURL: String = "",
    @SerializedName("webformatWidth")
    val webformatWidth: Int = 0,
    @SerializedName("webformatHeight")
    val webformatHeight: Int = 0,
    @SerializedName("largeImageURL")
    val largeImageURL: String = "",
    @SerializedName("imageWidth")
    val imageWidth: Int = 0,
    @SerializedName("imageHeight")
    val imageHeight: Int = 0,
    @SerializedName("imageSize")
    val imageSize: Int = 0,
    @SerializedName("views")
    val views: Int = 0,
    @SerializedName("downloads")
    val downloads: Int = 0,
    @SerializedName("favorites")
    val favorites: Int = 0,
    @SerializedName("likes")
    val likes: Int = 0,
    @SerializedName("comments")
    val comments: Int = 0,
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("user")
    val user: String = "",
    @SerializedName("userImageURL")
    val userImageURL: String = ""
):Serializable