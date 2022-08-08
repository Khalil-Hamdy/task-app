package com.khalil.taskapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.khalil.taskapp.databinding.ActivitySignInactivityBinding

class SignINActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("loginDataFile", 0)
        val editor = sharedPreferences.edit()

        //READ THE DATA FROM SHAREDPREFRENCES
        val userNameFromSharedPreferences = sharedPreferences.getString("userNameKey", null)
        val passwordFromSharedPreferences = sharedPreferences.getString("passwordKey", null)
        val rememberMeFromSharedPreferences = sharedPreferences.getBoolean("isCheckedKey", false)


        // Setting the result on the view
        binding.etUserName.setText(userNameFromSharedPreferences)
        binding.etPassword.setText(passwordFromSharedPreferences)
        binding.chkRememberMe.isChecked = rememberMeFromSharedPreferences
        Toast.makeText(this,"Data Retrieved Successfully",Toast.LENGTH_SHORT).show()

        binding.chkRememberMe.setOnClickListener {
            val userName = binding.etUserName.text.toString() // Getting the username
            val password = binding.etPassword.text.toString() // Getting the password
            val isChecked = binding.chkRememberMe.isChecked //Getting the checkbox value

            // WRITE TO THE SHAREDPREFRENCES
            editor.apply {
                putString("userNameKey", userName)
                putString("passwordKey", password)
                putBoolean("isCheckedKey", isChecked)
                apply() // Applying changes
            }
            Toast.makeText(this,"Data Saved Successfully",Toast.LENGTH_SHORT).show()

        }


        binding.txRegister.setOnClickListener{
            val intent = Intent(this, SignUPActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnLogin.setOnClickListener{
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}