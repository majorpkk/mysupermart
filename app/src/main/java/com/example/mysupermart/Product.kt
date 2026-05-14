package com.example.mysupermart

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONArray
import java.util.*

data class Product(
    val product_id: Int,
    val product_name: String,
    val product_description: String?,
    val product_cost: Int,
    val product_photo: String?
)

class ProductAdapter(private var productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), Filterable {

    private var productListFiltered: List<Product> = productList

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.product_name)
        val txtDesc: TextView = itemView.findViewById(R.id.product_description)
        val txtPrice: TextView = itemView.findViewById(R.id.product_cost)
        val imgProduct: ImageView = itemView.findViewById(R.id.product_photo)
        val btnAddToCart: Button = itemView.findViewById(R.id.btn_add_to_cart)
        val btnPurchase: Button = itemView.findViewById(R.id.purchase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productListFiltered[position]

        holder.txtName.text = product.product_name
        holder.txtDesc.text = product.product_description ?: "No description"
        holder.txtPrice.text = "Ksh ${product.product_cost}"

        val imageUrl = "https://dancan1.alwaysdata.net/static/images/${product.product_photo}"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgProduct)

        holder.btnAddToCart.setOnClickListener {
            CartManager.addProduct(product)
            Toast.makeText(
                holder.itemView.context,
                "${product.product_name} added to cart!",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.btnPurchase.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PaymentActivity::class.java).apply {
                putExtra("product_id", product.product_id)
                putExtra("product_name", product.product_name)
                putExtra("product_description", product.product_description)
                putExtra("product_cost", product.product_cost)
                putExtra("product_photo", product.product_photo)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""

                val filteredList = if (charString.isEmpty()) {
                    productList
                } else {
                    val resultList = mutableListOf<Product>()
                    for (product in productList) {
                        if (product.product_name.lowercase(Locale.ROOT)
                                .contains(charString.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(product)
                        }
                    }
                    resultList
                }

                return FilterResults().apply { values = filteredList }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                productListFiltered = results?.values as List<Product>
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun fromJsonArray(jsonArray: JSONArray): List<Product> {
            val list = mutableListOf<Product>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Product(
                        product_id = obj.getInt("product_id"),
                        product_name = obj.getString("product_name"),
                        product_description = obj.optString("product_description", ""),
                        product_cost = obj.getInt("product_cost"),
                        product_photo = obj.optString("product_photo", "")
                    )
                )
            }
            return list
        }
    }
}
