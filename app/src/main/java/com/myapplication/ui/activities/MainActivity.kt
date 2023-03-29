package com.myapplication.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myapplication.MoviesTunesApplication
import com.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dataSource = (application as MoviesTunesApplication).movieDatasource
    }
}
