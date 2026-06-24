package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.MainViewModel
import com.example.ui.UserSession
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.SecondaryGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val cartItems by viewModel.cartItems.collectAsState()
    val cartCount by viewModel.cartCount.collectAsState()
    val cartTotal by viewModel.cartTotalPrice.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedDietFilter by remember { mutableStateOf("Both") } // Both, Veg, Non-Veg

    var showCheckoutDialog by remember { mutableStateOf(false) }
    val session by viewModel.userSession.collectAsState()
    val defaultCustomerName = when (val s = session) {
        is UserSession.Consumer -> s.name
        else -> ""
    }
    var customerName by remember(defaultCustomerName) { mutableStateOf(defaultCustomerName) }
    var deliveryAddress by remember { mutableStateOf("") }
    var locationCoords by remember { mutableStateOf("") }
    var retrievingLocation by remember { mutableStateOf(false) }
    var orderPlacedSuccess by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (fineGranted || coarseGranted) {
                retrievingLocation = true
                retrieveDeviceLocation(context) { lat, lng ->
                    retrievingLocation = false
                    locationCoords = "$lat, $lng"
                    if (deliveryAddress.isBlank()) {
                        deliveryAddress = "Location Coordinates: ($lat, $lng)"
                    } else {
                        deliveryAddress = "$deliveryAddress\n(Coords: $lat, $lng)"
                    }
                    Toast.makeText(context, "Location retrieved successfully!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Location permission denied. Please enter details manually.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val categories = listOf("All", "Buckets", "Burgers", "Wraps", "Drinks")
    val dietFilters = listOf("Both", "Veg", "Non-Veg")

    // Full Menu List based on user prompt
    val fullMenu = listOf(
        MenuItemData(
            name = "Zinger Supreme",
            price = 349,
            category = "Burgers",
            isVeg = false,
            rating = "4.8",
            desc = "Signature fiery chicken breast, melted cheddar cheese, fresh lettuce, and signature cream sauce.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD4kiicMs9DTQ9qAjnXnHFAKLEBiUYNwAq06UzdBrMNY-1GgCB7RtD2-q__34q__8yhqtMEC7DpjAlT0VS6UEV4BugzLdxVsNSIbGf19n1UvVF71QDP_iuOXlopCpl99mI_NS9Pv2QP0iARSmi-MYFtUHOLfrKFntOOM6_k-Wdts3qCiBgnYoindYH4APsPMty_i3WwpPFVgTvlUC-RLxOePjF-jFfG0q1RaJx22gOMsw43pLRdnu3pncNLzYxFY2nSyxMv27FTe3Zl"
        ),
        MenuItemData(
            name = "Family Bucket",
            price = 1299,
            category = "Buckets",
            isVeg = false,
            rating = "4.9",
            desc = "A sharing feast of 8 gold-crispy chicken pieces, served with garlic paste, premium french fries, and fresh buns.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBJPY3ia5lbf4SVo3SaDCs0kIU3lfnh73qHlizBKL0AN04bNjfovunrnaarDxeMrd5aucnqy7APVpyvBSYES2S_onhjOhsWA0lN-75dm1l20OiWmqxGVR235fm2jZUODwkG8dubCEKz7b9dQi0pfOeBzXWZH8FQkfnqQXrHBbo5IcBV11yoXdrQgLh5RyT_eRaBdPkt8kuAV5uMpMdRnSSskQ7yWTn4FzeiifVdqiJBUDmLoT7yY4ytSaGvVIRx6-HFo_kYgRVy6CNm"
        ),
        MenuItemData(
            name = "Crispy Twister",
            price = 229,
            category = "Wraps",
            isVeg = false,
            rating = "4.6",
            desc = "Crispy golden tenders wrapped in flatbread with diced tomatoes, shredded lettuce, and rich garlic mayo.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDcHK7FWQR2O2hfa5xJS46zNohwo7AR2NVEe-j8diCedhErzz236iPjZJKfrPEmb5zB3O0Z-bKhL5odKv8KLB7AG7nNpwicU6ivTuOQulv_AuktBxRLboU8LUxUStLxAO8WZ0ZGSi020O5SfSmSnxlVO5HxKBKai2Wp_8R1WfyTll3oha68txo_9Ly5LLcpza240seTfrifPUkw7rxXjfmNwCjI7aYPEVXC6uVtiDI9uvAHCyPFZfYdD-Hfv07iMFy3_YGEvTsKZhbn"
        ),
        MenuItemData(
            name = "Saffron Cooler",
            price = 189,
            category = "Drinks",
            isVeg = true,
            rating = "4.7",
            desc = "A refreshing elixir of premium saffron, crushed mint, fresh lime, and mineral-infused bubbly soda.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBO8WmOmLJlLyfR8TALLu9vFuBM9_U-uNZ61KVk-6XutqOacF9hmaK_v91wuFylwSA34wZTwFIC7wSKKk316dnDN4IltNzsCZwZR9hzzOMSMXbSTRFoLn3lj2pLMjQnGRPXWXGprcoqQb3djldewaJDxRfWw6sro_JqE4NTNE8TxSUGMVB5SKaWfl5yEoIbhe67q00Rahw8jc4xiEoGAsuLlK9PqimqgJSyrauRueH6AapgVGtpcj5d6dsnQEvz4KCum_dxkE0iXqao"
        ),
        MenuItemData(
            name = "Classic Crispy",
            price = 499,
            category = "Buckets",
            isVeg = false,
            rating = "4.9",
            desc = "The original golden broasted chicken with 18 secret spices, super juicy on the inside, exceptionally crunchy outside.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDavtuVk_RDHNuB_xOW_TEiZcKo05FpMqO9jpcB-HsoAIEU-0kBHht8wy-o3EdrNC34budbaH5GzDBH4e2LKunfRF-q0398xil1q4_9ZebZyw8ZlC9FAk8E3FzoWm2ORqFRTFhN8wS-IdkFbEn26RfiBrdodxNX01aR7c5lNXzwnmJ2hLcevNPA7U5XXRnnpGKskMNJ0qBId8EhUayxNl1ZnLlHh1WSPobTIXOix_PvWlux1waSzxZ8ZOvFwfiNkgxPk2pFcjazh0xF"
        ),
        MenuItemData(
            name = "The Big Baik",
            price = 349,
            category = "Burgers",
            isVeg = false,
            rating = "4.7",
            desc = "A massive chicken breast fillet, cheese drip, pickles, and classic Al Baik sauce on toasted brioche.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA92X2aEWt7cikq0Lv8pOJnU7q9WvHnFRqcM-cY6api_yMZec-UctaxQHMD_X6ft_q5KzepqbYmM5ongKbNnde7OO3r0qoLlfPXMcrxBqqLbsWAQA4wQEbp7GScKjGcVLWu02UbrsWKPzVvDcVgDARbe3VEIG0Qs3_tKMJ4sorq3tO7CMf2fjztXMNmQDRqxR-GCwWCpIzSJUZ1Ge7ACXL_e4xpmUSY2K7QHl5-RU9OEygNaykq4U8b6upORaNb5TkxM2Xo1lSxhrGH"
        ),
        MenuItemData(
            name = "Spicy Zinger Roll",
            price = 289,
            category = "Wraps",
            isVeg = false,
            rating = "4.8",
            desc = "Succulent hot-crispy chicken tenders, sriracha glazed, wrapped inside flaky ghee paratha with lettuce.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA5oJEL5AY7od_v0KvSjlEBPAySUdMD-S3R3tSvASpMclXlHQxoPOGxQJOujr3QOKGDWQFkM633wnJhtQAE23VtZ4rs4oDbItI522lt0bI99qZZrjgwIB6UzPsk45N6jJAs40cEBwtS4WpsJ7IaJz5Ctan3eGqZ-hbeSdTlMBeEh3ogXq5FOr9ggjJOrq3Gg8HtY-P8gT_WiscOenxbrrmdyzZ7lUS38LxNaudiqdvAIUVgS9VtZOgzfqjT8CaJmI4bPZeDiiQUHD6r"
        )
    )

    // Filtering logic
    val filteredMenu = fullMenu.filter { item ->
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                item.desc.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || item.category.equals(selectedCategory, ignoreCase = true)
        val matchesDiet = when (selectedDietFilter) {
            "Veg" -> item.isVeg
            "Non-Veg" -> !item.isVeg
            else -> true
        }
        matchesSearch && matchesCategory && matchesDiet
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAjqW3NQhblDyqnfRQ6VeDaXLegeIWKmb_SLB8QuJWOJuz2OVruShCYiHK-f2IKYc7tefkHGQ9HJr6ZwQ0iPGBOYwHQwj_ARQ95LF9C7hajnBGIqPYbZQs3iaxai1lKWVC0vJVL9ou5CbFIOh6EPn-Qs7EX0brGDKlfKKypBJfTUSRh_4ALC3WMGdvkInqUrrX4hbpJ9A9BOvApu0xMSpkKo37aGH8UOV-Dwz7cv67dTp-o4fQAh2UEhubm3AUy_mj18YfC15-36_u5",
                    contentDescription = "Our Signature Menu",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color(0xFF0A0A0A)
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "OUR SIGNATURE MENU",
                        color = SecondaryGold,
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Curated Culinary Artistry",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Search and filters
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Favorite Cravings Search
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search your favorite cravings...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryCrimson,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0xFF131313),
                        unfocusedContainerColor = Color(0xFF131313)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PrimaryCrimson else Color(0xFF131313))
                                .border(
                                    BorderStroke(1.dp, if (isSelected) Color.Transparent else Color(0x1AFFFFFF)),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = category,
                                color = if (isSelected) Color.White else Color.LightGray,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Diet Filters (Veg, Non-Veg)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dietFilters.forEach { diet ->
                        val isSelected = selectedDietFilter == diet
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) SecondaryGold else Color(0xFF1C1B1B))
                                .clickable { selectedDietFilter = diet }
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = diet,
                                color = if (isSelected) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Grid
            if (filteredMenu.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No dishes found matching your selection.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredMenu) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(290.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                            border = BorderStroke(1.dp, Color(0x0FFFFFFF)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                ) {
                                    AsyncImage(
                                        model = item.imageUrl,
                                        contentDescription = item.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Veg/Non-veg Dot Badge
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(8.dp)
                                            .size(22.dp)
                                            .background(Color.White, RoundedCornerShape(4.dp))
                                            .border(
                                                BorderStroke(1.dp, if (item.isVeg) Color.Green else Color.Red),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    if (item.isVeg) Color.Green else Color.Red,
                                                    CircleShape
                                                )
                                        )
                                    }

                                    // Rating
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(8.dp)
                                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = "Rating",
                                            tint = SecondaryGold,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = item.rating,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = item.name,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleLarge,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.desc,
                                            color = Color.Gray,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 2
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "₹${item.price}",
                                            color = SecondaryGold,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleLarge
                                        )

                                        IconButton(
                                            onClick = {
                                                viewModel.addToCart(
                                                    name = item.name,
                                                    price = item.price,
                                                    isVeg = item.isVeg,
                                                    imageUrl = item.imageUrl
                                                )
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(PrimaryCrimson, CircleShape)
                                        ) {
                                            Icon(
                                                Icons.Default.ShoppingCart,
                                                contentDescription = "Add to Cart",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp)) // padding for Bottom Nav
        }

        // Sticky Mini Cart Checkout Drawer
        if (cartCount > 0) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 96.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(68.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xEE1E1E1E)),
                border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$cartCount Items Selected",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Total: $cartTotal SAR",
                            color = SecondaryGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = { showCheckoutDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Checkout",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Checkout & Placed dialog
        if (showCheckoutDialog) {
            Dialog(onDismissRequest = { showCheckoutDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                    border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!orderPlacedSuccess) {
                            Text(
                                "Confirm Your Order",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Items to be cooked freshly matching legendary standards of Al Baik.",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Cart Summary
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                cartItems.forEach { item ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "${item.quantity}x ${item.name}",
                                            color = Color.LightGray,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            "₹${item.price * item.quantity}",
                                            color = Color.White,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                Divider(color = Color(0x1AFFFFFF), modifier = Modifier.padding(vertical = 4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Grand Total", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("₹$cartTotal", color = SecondaryGold, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = customerName,
                                onValueChange = { customerName = it },
                                label = { Text("Your Name", color = Color.Gray) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryCrimson,
                                    unfocusedBorderColor = Color(0x1AFFFFFF),
                                    focusedContainerColor = Color(0xFF1C1B1B),
                                    unfocusedContainerColor = Color(0xFF1C1B1B)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = deliveryAddress,
                                onValueChange = { deliveryAddress = it },
                                label = { Text("Delivery Address", color = Color.Gray) },
                                minLines = 2,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryCrimson,
                                    unfocusedBorderColor = Color(0x1AFFFFFF),
                                    focusedContainerColor = Color(0xFF1C1B1B),
                                    unfocusedContainerColor = Color(0xFF1C1B1B)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Location Feature Option
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1C1B1B), RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Location Pin",
                                            tint = if (locationCoords.isNotEmpty()) SecondaryGold else Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "GPS Location Option",
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = if (locationCoords.isNotEmpty()) "Location Attached" else "Enable location for swift tracking",
                                                color = Color.Gray,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            val hasFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                            val hasCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                                            if (hasFineLocation || hasCoarseLocation) {
                                                retrievingLocation = true
                                                retrieveDeviceLocation(context) { lat, lng ->
                                                    retrievingLocation = false
                                                    locationCoords = "$lat, $lng"
                                                    if (deliveryAddress.isBlank()) {
                                                        deliveryAddress = "Location Coordinates: ($lat, $lng)"
                                                    } else {
                                                        if (!deliveryAddress.contains("Coords:")) {
                                                            deliveryAddress = "$deliveryAddress\n(Coords: $lat, $lng)"
                                                        }
                                                    }
                                                    Toast.makeText(context, "Location retrieved successfully!", Toast.LENGTH_SHORT).show()
                                                }
                                            } else {
                                                locationPermissionLauncher.launch(
                                                    arrayOf(
                                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                                    )
                                                )
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = if (locationCoords.isNotEmpty()) Color(0xFF4CAF50) else PrimaryCrimson),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        if (retrievingLocation) {
                                            CircularProgressIndicator(
                                                color = Color.White,
                                                modifier = Modifier.size(16.dp),
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.MyLocation,
                                                    contentDescription = "My Location",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (locationCoords.isNotEmpty()) "Refresh" else "Get GPS",
                                                    fontSize = 11.sp,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }

                                if (locationCoords.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Coordinates: $locationCoords",
                                        color = SecondaryGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { showCheckoutDialog = false },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel", color = Color.White)
                                }

                                Button(
                                    onClick = {
                                        if (customerName.isBlank()) {
                                            Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                                        } else if (deliveryAddress.isBlank()) {
                                            Toast.makeText(context, "Please enter your delivery address", Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.checkout(customerName, deliveryAddress, locationCoords)
                                            orderPlacedSuccess = true
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Place Order", color = Color.White)
                                }
                            }
                        } else {
                            // Success View
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0xFF4CAF50).copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Success",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                "Order Sent to Kitchen!",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Your order has been successfully placed! You can track its live progress (Pending -> Preparing -> Out for Delivery) in real-time under the new 'Cart' tab.",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    orderPlacedSuccess = false
                                    showCheckoutDialog = false
                                    customerName = ""
                                    deliveryAddress = ""
                                    locationCoords = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Awesome", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class MenuItemData(
    val name: String,
    val price: Int,
    val category: String,
    val isVeg: Boolean,
    val rating: String,
    val desc: String,
    val imageUrl: String
)

@SuppressLint("MissingPermission")
fun retrieveDeviceLocation(context: Context, onLocationRetrieved: (Double, Double) -> Unit) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    try {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            Toast.makeText(context, "Please turn on GPS/Location in system settings.", Toast.LENGTH_LONG).show()
            return
        }

        var location: Location? = null
        if (isNetworkEnabled) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (location == null && isGpsEnabled) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

        if (location != null) {
            onLocationRetrieved(location.latitude, location.longitude)
        } else {
            val provider = if (isNetworkEnabled) LocationManager.NETWORK_PROVIDER else LocationManager.GPS_PROVIDER
            locationManager.requestSingleUpdate(provider, object : LocationListener {
                override fun onLocationChanged(loc: Location) {
                    onLocationRetrieved(loc.latitude, loc.longitude)
                }
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }, Looper.getMainLooper())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Could not fetch GPS: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
