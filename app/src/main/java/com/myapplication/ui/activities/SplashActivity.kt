package com.myapplication.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.myapplication.R
import com.myapplication.databinding.ActivitySplashBinding
import com.myapplication.util.extension.startAnimationWithId

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivLogoBrand.startAnimationWithId(R.anim.rotate_scale)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            },
            3000,
        )
    }
}
