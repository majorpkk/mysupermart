package com.example.mysupermart

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Delay for 3 seconds then navigate to Homepage
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
