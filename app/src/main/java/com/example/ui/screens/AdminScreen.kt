package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import coil.compose.AsyncImage
import com.example.data.model.OrderRecord
import com.example.ui.MainViewModel
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.SecondaryGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: MainViewModel
) {
    val orders by viewModel.orders.collectAsState()

    // Real-time calculated metrics
    val totalSales = orders.sumOf { it.price }
    val activeOrdersCount = orders.count { it.status == "Pending" || it.status == "Preparing" }
    val customersCount = orders.map { it.customerName }.distinct().size

    // UI state for adding a mock order
    var showAddOrderDialog by remember { mutableStateOf(false) }
    var mockCustomerName by remember { mutableStateOf("") }
    var mockItemsText by remember { mutableStateOf("") }
    var mockPrice by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Welcome Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF131313))
                        .border(BorderStroke(1.dp, Color(0x0FFFFFFF)))
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "WELCOME BACK, ADMIN",
                                color = SecondaryGold,
                                style = MaterialTheme.typography.labelLarge,
                                letterSpacing = 3.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Today's Overview",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { viewModel.clearAllOrders() },
                                modifier = Modifier
                                    .background(Color(0xFF251F20), CircleShape)
                                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Clear All Orders",
                                    tint = PrimaryCrimson
                                )
                            }

                            IconButton(
                                onClick = { viewModel.logout() },
                                modifier = Modifier
                                    .background(Color(0xFF251F20), CircleShape)
                                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Logout,
                                    contentDescription = "Log Out",
                                    tint = SecondaryGold
                                )
                            }
                        }
                    }
                }
            }

            // Metrics Cards Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Today's Sales
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                        border = BorderStroke(1.dp, Color(0x1AFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("TODAY'S SALES", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("₹$totalSales", color = SecondaryGold, fontSize = 22.sp, fontWeight = FontWeight.Black)
                            Text("+14.2% from yesterday", color = Color(0xFF4CAF50), fontSize = 10.sp)
                        }
                    }

                    // Active Orders
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                        border = BorderStroke(1.dp, Color(0x1AFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ACTIVE ORDERS", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("$activeOrdersCount", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                            Text("Freshly prepared live", color = Color.LightGray, fontSize = 10.sp)
                        }
                    }

                    // Total Customers
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                        border = BorderStroke(1.dp, Color(0x1AFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("CUSTOMERS", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("$customersCount", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                            Text("Lal Chowk & Rajbagh", color = Color.LightGray, fontSize = 10.sp)
                        }
                    }
                }
            }

            // Custom Revenue Forecast Chart
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Hourly Revenue Forecast",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                        border = BorderStroke(1.dp, Color(0x0FFFFFFF))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // Render custom canvas bar chart
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val width = size.width
                                val height = size.height

                                // Draw baseline
                                drawLine(
                                    color = Color(0x33FFFFFF),
                                    start = Offset(0f, height),
                                    end = Offset(width, height),
                                    strokeWidth = 2f
                                )

                                // Data heights (percentages of total height)
                                val barData = listOf(0.35f, 0.55f, 0.40f, 0.85f, 0.70f, 0.95f, 0.60f)
                                val barCount = barData.size
                                val barGap = 24f
                                val totalGapsWidth = barGap * (barCount - 1)
                                val barWidth = (width - totalGapsWidth) / barCount

                                for (i in 0 until barCount) {
                                    val left = i * (barWidth + barGap)
                                    val top = height - (barData[i] * height)
                                    val barHeight = barData[i] * height

                                    drawRoundRect(
                                        brush = Brush.verticalGradient(
                                            colors = if (i == 5) {
                                                listOf(PrimaryCrimson, SecondaryGold) // Highlight peak hour
                                            } else {
                                                listOf(Color(0xFF35151A), PrimaryCrimson)
                                            }
                                        ),
                                        topLeft = Offset(left, top),
                                        size = Size(barWidth, barHeight),
                                        cornerRadius = CornerRadius(8f, 8f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("12 PM", "2 PM", "4 PM", "6 PM", "8 PM", "10 PM", "12 AM").forEach { hour ->
                            Text(hour, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Best Seller Banner Promos
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Best Selling Campaign",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1415)),
                        border = BorderStroke(1.dp, PrimaryCrimson.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAjqW3NQhblDyqnfRQ6VeDaXLegeIWKmb_SLB8QuJWOJuz2OVruShCYiHK-f2IKYc7tefkHGQ9HJr6ZwQ0iPGBOYwHQwj_ARQ95LF9C7hajnBGIqPYbZQs3iaxai1lKWVC0vJVL9ou5CbFIOh6EPn-Qs7EX0brGDKlfKKypBJfTUSRh_4ALC3WMGdvkInqUrrX4hbpJ9A9BOvApu0xMSpkKo37aGH8UOV-Dwz7cv67dTp-o4fQAh2UEhubm3AUy_mj18YfC15-36_u5",
                                contentDescription = "Classic Broast",
                                modifier = Modifier
                                    .width(130.dp)
                                    .fillMaxHeight(),
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "Classic Broast",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .background(SecondaryGold, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "Active",
                                                color = Color.Black,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Marinated for 18 hours in 18 secret spices.",
                                        color = Color.Gray,
                                        fontSize = 11.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Units",
                                        tint = SecondaryGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "5,420+ Units Sold This Week • Highly Popular in Rajbagh",
                                        color = Color.LightGray,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Recent Orders Title
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Recent Orders Table",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Click on status badges to cycle preparing status. Click delete to remove record.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // List of Orders
            if (orders.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No active customer orders currently logged.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                        border = BorderStroke(1.dp, Color(0x0FFFFFFF)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Initials Avatar
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color(0xFF2A1C1D), CircleShape)
                                    .border(BorderStroke(1.dp, PrimaryCrimson.copy(alpha = 0.4f)), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = order.avatarInitials,
                                    color = PrimaryCrimson,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Order Info
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = order.customerName,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = order.orderId,
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = order.items,
                                    color = Color.LightGray,
                                    fontSize = 13.sp,
                                    maxLines = 2
                                )

                                if (order.deliveryAddress.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📍 Delivery: ${order.deliveryAddress}",
                                        color = Color(0xFFE0E0E0),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (order.locationCoords.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "🌐 Coordinates: ${order.locationCoords}",
                                        color = SecondaryGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "₹${order.price}",
                                        color = SecondaryGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    // Cycleable Status Badge
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                when (order.status) {
                                                    "Pending" -> Color(0xFFE9C349).copy(alpha = 0.15f)
                                                    "Preparing" -> Color(0xFFFFB3B1).copy(alpha = 0.15f)
                                                    "Out for Delivery" -> Color(0xFFE31837).copy(alpha = 0.15f)
                                                    else -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                                                }
                                            )
                                            .border(
                                                BorderStroke(
                                                    1.dp,
                                                    when (order.status) {
                                                        "Pending" -> Color(0xFFE9C349)
                                                        "Preparing" -> Color(0xFFFFB3B1)
                                                        "Out for Delivery" -> Color(0xFFE31837)
                                                        else -> Color(0xFF4CAF50)
                                                    }
                                                ),
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable {
                                                val nextStatus = when (order.status) {
                                                    "Pending" -> "Preparing"
                                                    "Preparing" -> "Out for Delivery"
                                                    "Out for Delivery" -> "Completed"
                                                    else -> "Pending"
                                                }
                                                viewModel.updateOrderStatus(order.orderId, nextStatus)
                                            }
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = order.status,
                                            color = when (order.status) {
                                                "Pending" -> Color(0xFFE9C349)
                                                "Preparing" -> Color(0xFFFFB3B1)
                                                "Out for Delivery" -> Color(0xFFE31837)
                                                else -> Color(0xFF4CAF50)
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Delete Action
                            IconButton(onClick = { viewModel.deleteOrderById(order.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete Order",
                                    tint = Color.Gray.copy(alpha = 0.5f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Server Indicator
            item {
                Spacer(modifier = Modifier.height(28.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "System Live | Srinagar Server: Healthy",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Add Mock Order FAB
        FloatingActionButton(
            onClick = { showAddOrderDialog = true },
            containerColor = PrimaryCrimson,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 92.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Mock Order")
        }

        // Add Mock Order Dialog
        if (showAddOrderDialog) {
            Dialog(onDismissRequest = { showAddOrderDialog = false }) {
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
                        Text(
                            "Add Walk-in Order",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = mockCustomerName,
                            onValueChange = { mockCustomerName = it },
                            label = { Text("Customer Name", color = Color.Gray) },
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
                            value = mockItemsText,
                            onValueChange = { mockItemsText = it },
                            label = { Text("Items (e.g. 2x Zinger, 1x Pepsi)", color = Color.Gray) },
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
                            value = mockPrice,
                            onValueChange = { mockPrice = it },
                            label = { Text("Price (e.g. 599)", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryCrimson,
                                unfocusedBorderColor = Color(0x1AFFFFFF),
                                focusedContainerColor = Color(0xFF1C1B1B),
                                unfocusedContainerColor = Color(0xFF1C1B1B)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showAddOrderDialog = false },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    val priceVal = mockPrice.toIntOrNull() ?: 0
                                    viewModel.addOrder(
                                        customerName = mockCustomerName,
                                        itemsText = mockItemsText,
                                        price = priceVal
                                    )
                                    showAddOrderDialog = false
                                    mockCustomerName = ""
                                    mockItemsText = ""
                                    mockPrice = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Add Order", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
