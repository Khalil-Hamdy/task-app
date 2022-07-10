package com.khalil.taskapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.khalil.taskapp.databinding.ActivitySignInactivityBinding

class SignINActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txRegister.setOnClickListener{
            val intent = Intent(this, SignUPActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}