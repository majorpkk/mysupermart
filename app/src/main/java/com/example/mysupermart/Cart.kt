package com.example.mysupermart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Cart : AppCompatActivity() {

    private lateinit var totalPriceTxt: TextView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var summaryCard: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        // Window insets handling
        val mainLayout = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        recyclerView = findViewById(R.id.cart_recyclerview)
        totalPriceTxt = findViewById(R.id.txt_total)
        emptyStateLayout = findViewById(R.id.layout_empty_cart)
        summaryCard = findViewById(R.id.card_summary)
        val btnStartShopping = findViewById<Button>(R.id.btn_start_shopping)
        val btnCheckout = findViewById<Button>(R.id.btn_checkout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter
        val adapter = CartAdapter(CartManager.getCartItemsList()) {
            updateUI()
        }

        recyclerView.adapter = adapter
        
        btnStartShopping.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        btnCheckout.setOnClickListener {
            // Future checkout logic
        }

        updateUI()
    }

    private fun updateUI() {
        val total = CartManager.getTotalPrice()
        totalPriceTxt.text = "Ksh $total"

        if (CartManager.getUniqueItemCount() == 0) {
            emptyStateLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            summaryCard.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            summaryCard.visibility = View.VISIBLE
        }


    }
}
