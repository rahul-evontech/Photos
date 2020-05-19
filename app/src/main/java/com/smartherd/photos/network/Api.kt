package com.smartherd.photos.network


import com.smartherd.photos.models.Photos
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("api/")
    suspend fun getPhotos(
        @Query("page")
        pageNo:Int = 1,
        @Query("key")
        key:String = "16540389-af57e86184884632c601e38b6"

    ): Response<Photos>

    @GET("api/")
    suspend fun searchPhotos(
        @Query("q")
        search: String,
        @Query("page")
        pageNo: Int = 1,
        @Query("key")
        key:String =  "16540389-af57e86184884632c601e38b6"
    ): Response<Photos>



    companion object{
        operator fun invoke(): Api {

            val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder().addInterceptor(logger)

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .baseUrl("https://pixabay.com/")
                .build()
                .create(Api::class.java)

        }
    }
}