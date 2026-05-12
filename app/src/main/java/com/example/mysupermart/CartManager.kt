package com.example.mysupermart

object CartManager {
    // Map to hold Product and its specific quantity
    private val cartItems = mutableMapOf<Product, Int>()

    fun addProduct(product: Product) {
        val currentQuantity = cartItems[product] ?: 0
        cartItems[product] = currentQuantity + 1
    }

    fun removeProduct(product: Product) {
        val currentQuantity = cartItems[product] ?: 0
        if (currentQuantity > 1) {
            cartItems[product] = currentQuantity - 1
        } else {
            cartItems.remove(product)
        }
    }

    fun deleteProductCompletely(product: Product) {
        cartItems.remove(product)
    }

    fun getCartItems(): Map<Product, Int> = cartItems.toMap()

    // Helper for adapters that expect a simple list of products
    fun getCartItemsList(): MutableList<Product> = cartItems.keys.toMutableList()

    fun getTotalPrice(): Int = getTotalCost()

    fun getTotalCost(): Int {
        return cartItems.entries.sumOf { (product, quantity) -> 
            product.product_cost * quantity 
        }
    }

    fun getUniqueItemCount(): Int = cartItems.size

    fun getTotalItemCount(): Int = cartItems.values.sum()

    fun clearCart() {
        cartItems.clear()
    }
}
