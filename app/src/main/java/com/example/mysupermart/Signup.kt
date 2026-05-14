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

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val username = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val phone = findViewById<EditText>(R.id.phone)
        val signupbutton = findViewById<Button>(R.id.signup)
        val signintext = findViewById<TextView>(R.id.signintext)

        signintext.setOnClickListener {
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }
        
        signupbutton.setOnClickListener {
            val helper = ApiHelper(applicationContext)
            val api = "https://dancan1.alwaysdata.net/api/signup"

            val data = RequestParams()
            data.put("username", username.text.toString())
            data.put("email", email.text.toString())
            data.put("password", password.text.toString())
            data.put("phone", phone.text.toString())

            helper.post(api, data)

        }

        if ("message"=="success"){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
