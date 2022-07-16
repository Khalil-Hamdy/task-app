package com.khalil.taskapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.khalil.taskapp.databinding.ActivitySplashScreanBinding


class SplashScreanActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val intent = Intent(this, SignINActivity::class.java)
            startActivity(intent)

            finish()
        },2000)

    }
}