package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.UserSession
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.BranchesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.SecondaryGold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer() {
    val viewModel: MainViewModel = viewModel()
    val session by viewModel.userSession.collectAsState()

    if (session == null) {
        OnboardingScreen(viewModel = viewModel)
    } else if (session is UserSession.Admin) {
        AdminScreen(viewModel = viewModel)
    } else {
        var selectedTab by remember { mutableStateOf("Home") }
        val cartCount by viewModel.cartCount.collectAsState()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xF2111111), // 95% opacity charcoal
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(1.dp, Color(0x1AFFFFFF)))
                ) {
                    // Home Tab
                    NavigationBarItem(
                        selected = selectedTab == "Home",
                        onClick = { selectedTab = "Home" },
                        label = { Text("Home", fontSize = 11.sp, fontWeight = if (selectedTab == "Home") androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PrimaryCrimson,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PrimaryCrimson
                        ),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        }
                    )

                    // Menu Tab
                    NavigationBarItem(
                        selected = selectedTab == "Menu",
                        onClick = { selectedTab = "Menu" },
                        label = { Text("Menu", fontSize = 11.sp, fontWeight = if (selectedTab == "Menu") androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PrimaryCrimson,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PrimaryCrimson
                        ),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = "Menu"
                            )
                        }
                    )

                    // Branches Tab
                    NavigationBarItem(
                        selected = selectedTab == "Branches",
                        onClick = { selectedTab = "Branches" },
                        label = { Text("Branches", fontSize = 11.sp, fontWeight = if (selectedTab == "Branches") androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PrimaryCrimson,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PrimaryCrimson
                        ),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Branches"
                            )
                        }
                    )

                    // Cart Tab
                    NavigationBarItem(
                        selected = selectedTab == "Cart",
                        onClick = { selectedTab = "Cart" },
                        label = { Text("Cart", fontSize = 11.sp, fontWeight = if (selectedTab == "Cart") androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PrimaryCrimson,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PrimaryCrimson
                        ),
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (cartCount > 0) {
                                        Badge(
                                            containerColor = SecondaryGold,
                                            contentColor = Color.Black
                                        ) {
                                            Text("$cartCount", fontSize = 9.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Cart"
                                )
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    "Home" -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToMenu = { selectedTab = "Menu" },
                        onNavigateToBranches = { selectedTab = "Branches" }
                    )
                    "Menu" -> MenuScreen(viewModel = viewModel)
                    "Branches" -> BranchesScreen()
                    "Cart" -> CartScreen(
                        viewModel = viewModel,
                        onNavigateToMenu = { selectedTab = "Menu" }
                    )
                }
            }
        }
    }
}
