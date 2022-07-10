package com.khalil.taskapp

//import android.support.v7.app.AppCompatActivity;

import android.content.Intent
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.khalil.taskapp.databinding.ActivitySignupBinding

class SignUPActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txRegister.setOnClickListener {
            val intent = Intent(this, SignINActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}