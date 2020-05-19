package com.smartherd.photos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smartherd.photos.models.Hit
import com.smartherd.photos.models.Photos

@Database(entities = [Hit::class],
    version = 1
    )
abstract class PhotoDatabase: RoomDatabase() {

    abstract fun getDao(): PhotosDao

    companion object{
        @Volatile
        private var instance : PhotoDatabase? = null

        private val LOCK = Any()


        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: dataBaseBuilder(
                        context
                    ).also {
                        instance = it
                    }
            }
        private fun dataBaseBuilder(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            "database.db"
        ).build()
    }
}