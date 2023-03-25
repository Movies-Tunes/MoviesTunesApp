package com.myapplication.data.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapplication.data.entities.TopRatedResultItem

@Database([TopRatedResultItem::class], version = 1)
abstract class MoviesTunesDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
    companion object{
        @Volatile
        private var instance: MoviesTunesDatabase? = null
        fun getInstance(context: Context): MoviesTunesDatabase {
            if (instance == null){
                instance = Room.databaseBuilder(
                    context,
                    MoviesTunesDatabase::class.java,
                    "movies_db"
                ).build()
            }
            return instance as MoviesTunesDatabase
        }
    }
}
