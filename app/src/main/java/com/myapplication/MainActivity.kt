package com.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.myapplication.core.Constants
import com.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val service = (application as MoviesTunesApplication).moviesService

        lifecycleScope.launch {
            val topRatedMovies = service.getTopRatedMovies(Constants.API_KEY, 1)
            Log.e("Movies", topRatedMovies.toString())
            val movieDetail = service.getMovieDetails(topRatedMovies.results[0].id, Constants.API_KEY)
            Log.e("Detail", movieDetail.toString())
        }
    }
}
