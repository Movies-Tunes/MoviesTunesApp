package com.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dataSource = (application as MoviesTunesApplication).movieDatasource

        binding.btnRefresh.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val topRatedMovies = dataSource.getAllMovies(1)
                Log.e("Movies", topRatedMovies.toString())
                dataSource.getMovieDetails(topRatedMovies[0].id).apply{
                    Log.e("Detail", this.toString())
                    runOnUiThread {
                        binding.tvHelloWorld.text = this.toString()
                    }
                }
            }
        }
    }
}
