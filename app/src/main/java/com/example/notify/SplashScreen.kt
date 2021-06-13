package com.example.notify

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this@SplashScreen, authenticationpage::class.java))
            finish()
        }, 500)
    }
}