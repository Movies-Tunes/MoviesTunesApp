package com.myapplication.data.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myapplication.data.entities.GenreItem
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.util.converters.TypeConverter

@Database([TopRatedResultItem::class, MovieDetail::class, GenreItem::class], version = 3)
@TypeConverters(TypeConverter::class)
abstract class MoviesTunesDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
    companion object {
        @Volatile
        private var instance: MoviesTunesDatabase? = null
        fun getInstance(context: Context): MoviesTunesDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    MoviesTunesDatabase::class.java,
                    "movies_db",
                ).addTypeConverter(TypeConverter())
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as MoviesTunesDatabase
        }
    }
}
