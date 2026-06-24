package com.example.data.repository

import com.example.data.dao.AppDao
import com.example.data.model.CartItem
import com.example.data.model.OrderRecord
import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    val cartItems: Flow<List<CartItem>> = appDao.getAllCartItems()
    val orders: Flow<List<OrderRecord>> = appDao.getAllOrders()

    suspend fun insertCartItem(cartItem: CartItem) {
        appDao.insertCartItem(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        appDao.updateCartItem(cartItem)
    }

    suspend fun deleteCartItem(cartItem: CartItem) {
        appDao.deleteCartItem(cartItem)
    }

    suspend fun clearCart() {
        appDao.clearCart()
    }

    suspend fun insertOrder(order: OrderRecord) {
        appDao.insertOrder(order)
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        appDao.updateOrderStatus(orderId, newStatus)
    }

    suspend fun deleteOrderById(id: Int) {
        appDao.deleteOrderById(id)
    }

    suspend fun clearAllOrders() {
        appDao.clearAllOrders()
    }
}
