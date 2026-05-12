package com.example.mysupermart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loopj.android.http.RequestParams

class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Session Check: If already logged in, skip this screen
        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        if (prefs.contains("email")) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Views
        val logo = findViewById<ImageView>(R.id.iv_logo)
        val welcomeText = findViewById<TextView>(R.id.tv_welcome)
        val subtitle = findViewById<TextView>(R.id.tv_subtitle)
        val loginCard = findViewById<androidx.cardview.widget.CardView>(R.id.cv_login)
        
        val signin = findViewById<Button>(R.id.signin)
        val signuptext = findViewById<TextView>(R.id.sign_up)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val guest = findViewById<TextView>(R.id.guest)

        // 2. Entrance Animations
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1200
        
        logo.startAnimation(fadeIn)
        welcomeText.startAnimation(fadeIn)
        subtitle.startAnimation(fadeIn)
        loginCard.startAnimation(fadeIn)

        signin.setOnClickListener {
            val nameStr = username.text.toString().trim()
            val passStr = password.text.toString().trim()
            
            // 3. Validation Logic
            if (nameStr.isEmpty()) {
                username.error = "Username required"
                username.requestFocus()
                return@setOnClickListener
            }
            if (passStr.isEmpty()) {
                password.error = "Password required"
                password.requestFocus()
                return@setOnClickListener
            }

            val helper = ApiHelper(applicationContext)
            val api = "https://paamajor1.alwaysdata.net/api/signin"

            val data = RequestParams()
            data.put("username", nameStr)
            data.put("password", passStr)

            helper.post_login(api, data)
        }

        signuptext.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        guest.setOnClickListener {
            // Clear activity stack for guest access
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
