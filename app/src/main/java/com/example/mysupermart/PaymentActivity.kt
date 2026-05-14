package com.example.mysupermart

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.loopj.android.http.RequestParams

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val productname=intent.getStringExtra("product_name")
        val productdescription=intent.getStringExtra("product_description")
        val productcost=intent.getIntExtra("product_cost", 0)
        val productphoto=intent.getStringExtra("product_photo")



//        fetch the views
        val image=findViewById<ImageView>(R.id.image)
        val name=findViewById<TextView>(R.id.product_name)
        val description=findViewById<TextView>(R.id.product_description)
        val cost=findViewById<TextView>(R.id.product_cost)
        val phone=findViewById<TextView>(R.id.phone)
        val purchasebutton=findViewById<Button>(R.id.purchase )

        name.text=productname
        description.text=productdescription
        cost.text="ksh $productcost"

        val imageUrl = "https://dancan.alwaysdata.net/static/images/${productphoto}"

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(image)

        val api="https://dancan1.alwaysdata.net/api/mpesa_payment"

        purchasebutton.setOnClickListener {
            val helper= ApiHelper(this)

            val data= RequestParams()

            data.put("phone",phone.text.toString())
            data.put("amount",productcost)

            helper.post(api,data)
        }







    }
}