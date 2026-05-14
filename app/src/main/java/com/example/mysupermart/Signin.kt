package com.example.mysupermart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loopj.android.http.RequestParams

class Signin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val signinbutton = findViewById<Button>(R.id.signin)
        val signuptext = findViewById<TextView>(R.id.signuptext)

        signinbutton.setOnClickListener {
            // Initialize ApiHelper
            val helper = ApiHelper(applicationContext)
            val api = "https://dancan1.alwaysdata.net/api/signin"

            // Create RequestParams
            val data = RequestParams()
            data.put("email", email.text.toString().trim())
            data.put("password", password.text.toString().trim())

            helper.post_login(api, data)
        }

        signuptext.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }
}
