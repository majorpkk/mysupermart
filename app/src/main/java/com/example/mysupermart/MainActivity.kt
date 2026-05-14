package com.example.mysupermart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1. Setup Window Insets properly on DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)
        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Sidebar (NavigationView) Setup
        val menu = findViewById<ImageButton>(R.id.menu)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        updateNavHeader(navView)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_cart -> {
                    startActivity(Intent(this, Cart::class.java))
                }
                R.id.nav_home -> { /* Already on Home */ }
                R.id.nav_orders -> { /* Open Orders Activity */ }
                R.id.nav_signin -> {
                    startActivity(Intent(this, Signin::class.java))
                }
                R.id.nav_signup -> {
                    startActivity(Intent(this, Signup::class.java))
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        menu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 3. Categories Setup
        setupCategories()

        // 4. API and Products RecyclerView Setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val progressBar = findViewById<ProgressBar>(R.id.progressbar)
        val api = "https://dancan1.alwaysdata.net/api/get_product"

        val helper = ApiHelper(applicationContext)
        helper.loadProducts(api, recyclerView, progressBar)

        // 5. Search Functionality
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (recyclerView.adapter as? ProductAdapter)?.filter?.filter(newText)
                return true
            }
        })

        // 6. Cart Button Navigation
        val cartBtn = findViewById<ImageButton>(R.id.btn_go_to_cart_top)
        cartBtn.setOnClickListener {
            startActivity(Intent(this, Cart::class.java))
        }
    }

    private fun updateNavHeader(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tv_user_name)
        val tvUserEmail = headerView.findViewById<TextView>(R.id.tv_user_email)

        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "Guest")
        val email = prefs.getString("email", "sign in to your account")

        tvUserName.text = "Welcome, $username"
        tvUserEmail.text = email
    }

    private fun setupCategories() {
        val rvCategories = findViewById<RecyclerView>(R.id.rv_categories)
        val categories = listOf(
            Category(1, "All", R.drawable.baseline_shopping_cart_24),
            Category(2, "Fruits", R.drawable.ic_launcher_foreground),
            Category(3, "Vegies", R.drawable.ic_launcher_foreground),
            Category(4, "Dairy", R.drawable.ic_launcher_foreground),
            Category(5, "Bakery", R.drawable.ic_launcher_foreground),
            Category(6, "Meat", R.drawable.ic_launcher_foreground)
        )
        
        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = CategoryAdapter(categories)
    }

    private fun logout() {
        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        CartManager.clearCart()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Signin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
