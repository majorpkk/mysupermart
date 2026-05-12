package com.example.mysupermart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val cartItems: MutableList<Product>,
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.cart_product_image)
        val productName: TextView = itemView.findViewById(R.id.cart_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.cart_product_price)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btn_remove_from_cart)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btn_plus)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btn_minus)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tv_item_subtotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartItems[position]
        val quantity = CartManager.getCartItems()[product] ?: 0
        val subtotal = product.product_cost * quantity

        holder.productName.text = product.product_name
        holder.productPrice.text = "Ksh ${product.product_cost}"
        holder.tvQuantity.text = quantity.toString()
        holder.tvSubtotal.text = "Subtotal: Ksh $subtotal"

        val imageUrl = "https://paamajor1.alwaysdata.net/static/images/${product.product_photo}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.productImage)

        holder.btnPlus.setOnClickListener {
            CartManager.addProduct(product)
            notifyItemChanged(holder.bindingAdapterPosition)
            onCartChanged()
        }

        holder.btnMinus.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                CartManager.removeProduct(product)
                if (CartManager.getCartItems()[product] == null) {
                    cartItems.removeAt(currentPos)
                    notifyItemRemoved(currentPos)
                } else {
                    notifyItemChanged(currentPos)
                }
                onCartChanged()
            }
        }

        holder.btnRemove.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                CartManager.deleteProductCompletely(product)
                cartItems.removeAt(currentPos)
                notifyItemRemoved(currentPos)
                onCartChanged()
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
