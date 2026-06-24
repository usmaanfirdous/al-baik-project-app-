package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.CartItem
import com.example.data.model.OrderRecord
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class UserSession {
    data class Consumer(val name: String, val phone: String, val loginType: String) : UserSession()
    object Admin : UserSession()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository
    private val sharedPrefs = application.getSharedPreferences("albaik_prefs", Context.MODE_PRIVATE)

    val cartItems: StateFlow<List<CartItem>>
    val orders: StateFlow<List<OrderRecord>>

    val cartCount: StateFlow<Int>
    val cartTotalPrice: StateFlow<Int>

    val userSession = MutableStateFlow<UserSession?>(null)

    init {
        val appDao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(appDao)

        // Load persisted user session
        val name = sharedPrefs.getString("user_name", null)
        val phone = sharedPrefs.getString("user_phone", null)
        val loginType = sharedPrefs.getString("user_login_type", null)
        val isAdmin = sharedPrefs.getBoolean("user_is_admin", false)

        if (isAdmin) {
            userSession.value = UserSession.Admin
        } else if (name != null && phone != null) {
            userSession.value = UserSession.Consumer(name, phone, loginType ?: "Phone")
        }

        cartItems = repository.cartItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        orders = repository.orders.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        cartCount = cartItems.map { list ->
            list.sumOf { it.quantity }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

        cartTotalPrice = cartItems.map { list ->
            list.sumOf { it.price * it.quantity }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

        // Seed initial orders to match user mock layout if database is empty
        viewModelScope.launch {
            val currentOrders = repository.orders.first()
            if (currentOrders.isEmpty()) {
                seedMockOrders()
            }
        }
    }

    private suspend fun seedMockOrders() {
        // Oct 24, 2024 timestamp approx: 1729728000000
        repository.insertOrder(
            OrderRecord(
                orderId = "#AB-9421",
                customerName = "Zaid Ahmed",
                items = "2x Spicy Broast, 1x Pepsi",
                price = 1240,
                status = "Preparing",
                timestamp = 1729728000000,
                avatarInitials = "ZA"
            )
        )
        repository.insertOrder(
            OrderRecord(
                orderId = "#AB-9420",
                customerName = "Sara Malik",
                items = "1x Family Meal Box",
                price = 2850,
                status = "Out for Delivery",
                timestamp = 1729727500000,
                avatarInitials = "SM"
            )
        )
        repository.insertOrder(
            OrderRecord(
                orderId = "#AB-9419",
                customerName = "Rayyan UI",
                items = "3x Classic Burgers",
                price = 890,
                status = "Pending",
                timestamp = 1729727000000,
                avatarInitials = "RU"
            )
        )
    }

    fun addToCart(name: String, price: Int, isVeg: Boolean, imageUrl: String) {
        viewModelScope.launch {
            val currentList = cartItems.value
            val existingItem = currentList.find { it.name == name }
            if (existingItem != null) {
                repository.insertCartItem(existingItem.copy(quantity = existingItem.quantity + 1))
            } else {
                repository.insertCartItem(
                    CartItem(
                        name = name,
                        price = price,
                        quantity = 1,
                        imageUrl = imageUrl,
                        isVeg = isVeg
                    )
                )
            }
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                repository.insertCartItem(item.copy(quantity = item.quantity - 1))
            } else {
                repository.deleteCartItem(item)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun checkout(customerName: String, deliveryAddress: String = "", locationCoords: String = "") {
        viewModelScope.launch {
            val currentCart = cartItems.value
            if (currentCart.isEmpty()) return@launch

            val itemsString = currentCart.joinToString(", ") { "${it.quantity}x ${it.name}" }
            val totalPrice = currentCart.sumOf { it.price * it.quantity }
            val randomNum = Random.nextInt(9000, 9999)
            val orderId = "#AB-$randomNum"

            val initials = if (customerName.length >= 2) {
                customerName.substring(0, 2).uppercase()
            } else if (customerName.isNotEmpty()) {
                customerName.take(1).uppercase() + "X"
            } else {
                "OK"
            }

            repository.insertOrder(
                OrderRecord(
                    orderId = orderId,
                    customerName = customerName.ifBlank { "Guest Customer" },
                    items = itemsString,
                    price = totalPrice,
                    status = "Pending",
                    timestamp = System.currentTimeMillis(),
                    avatarInitials = initials,
                    deliveryAddress = deliveryAddress,
                    locationCoords = locationCoords
                )
            )

            // Clear the cart on successful checkout
            repository.clearCart()
        }
    }

    fun addOrder(customerName: String, itemsText: String, price: Int) {
        viewModelScope.launch {
            val randomNum = Random.nextInt(9000, 9999)
            val orderId = "#AB-$randomNum"
            val initials = if (customerName.length >= 2) {
                customerName.substring(0, 2).uppercase()
            } else if (customerName.isNotEmpty()) {
                customerName.take(1).uppercase()
            } else {
                "AD"
            }

            repository.insertOrder(
                OrderRecord(
                    orderId = orderId,
                    customerName = customerName.ifBlank { "Walk-in Customer" },
                    items = itemsText.ifBlank { "Custom Admin Menu Order" },
                    price = if (price > 0) price else Random.nextInt(200, 1500),
                    status = "Pending",
                    timestamp = System.currentTimeMillis(),
                    avatarInitials = initials
                )
            )
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun deleteOrderById(id: Int) {
        viewModelScope.launch {
            repository.deleteOrderById(id)
        }
    }

    fun clearAllOrders() {
        viewModelScope.launch {
            repository.clearAllOrders()
        }
    }

    fun loginAsConsumer(name: String, phone: String, loginType: String) {
        sharedPrefs.edit()
            .putString("user_name", name)
            .putString("user_phone", phone)
            .putString("user_login_type", loginType)
            .putBoolean("user_is_admin", false)
            .apply()
        userSession.value = UserSession.Consumer(name, phone, loginType)
    }

    fun loginAsAdmin() {
        sharedPrefs.edit()
            .putBoolean("user_is_admin", true)
            .apply()
        userSession.value = UserSession.Admin
    }

    fun logout() {
        sharedPrefs.edit().clear().apply()
        userSession.value = null
    }
}
