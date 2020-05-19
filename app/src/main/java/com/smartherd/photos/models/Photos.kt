package com.smartherd.photos.models


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Photos(
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalHits")
    val totalHits: Int = 0,
    @SerializedName("hits")
    val hits: MutableList<Hit> = mutableListOf()
)