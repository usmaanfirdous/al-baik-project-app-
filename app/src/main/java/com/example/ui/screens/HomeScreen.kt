package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.MainViewModel
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.SecondaryGold

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToMenu: () -> Unit,
    onNavigateToBranches: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        // Main Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuC_NkgEpYeeKZazm20brC-hBplvEzIKwKfB2q0ru3VX16nPA589PxiDIRalr2994eCv1q-T4UG52a2G2wHyIuv1lyYozMfXx2l-2_hY7HezPwnivW3oMMWqr-Pch48MWUbJRcc8d5AAp-ywSO1J2B6h8eQtix26Qs-gYOdwQAu48popnAe9lcktiTtJNvR3Il5L7VC7qFeWYVwrXUD-Oj_DVYKSJVuWU6vQWBc1zV_kpLuqwpDZ3_iVtJLsbXBDJ48Jr_AeJk-gJ_dy",
                    contentDescription = "Premium Fried Chicken Hero Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark Gradients overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    Color(0xFF0A0A0A)
                                )
                            )
                        )
                )

                // Hero Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "PREMIUM FAST CASUAL",
                        color = SecondaryGold,
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "The Gold Standard of Crispy Perfection",
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Experience the world-renowned flavor of Al Baik, now reimagined with a luxury touch in the heart of Srinagar. Gold-crisp, succulent, and legendary.",
                        color = Color(0xFFE6BDBB),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onNavigateToMenu,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .weight(1f)
                        ) {
                            Text(
                                "View Menu",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        OutlinedButton(
                            onClick = onNavigateToBranches,
                            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            modifier = Modifier
                                .height(52.dp)
                                .weight(1f)
                        ) {
                            Text(
                                "Our Story",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Signature Dishes Carousel Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Signature Dishes",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(4.dp)
                        .background(PrimaryCrimson, RoundedCornerShape(2.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Carousel List
            val signatureItems = listOf(
                SignatureItem(
                    name = "Classic Crispy",
                    price = 499,
                    tag = "Best Seller",
                    desc = "Our world-famous secret recipe chicken, pressure fried to golden perfection with 18 secret spices.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDavtuVk_RDHNuB_xOW_TEiZcKo05FpMqO9jpcB-HsoAIEU-0kBHht8wy-o3EdrNC34budbaH5GzDBH4e2LKunfRF-q0398xil1q4_9ZebZyw8ZlC9FAk8E3FzoWm2ORqFRTFhN8wS-IdkFbEn26RfiBrdodxNX01aR7c5lNXzwnmJ2hLcevNPA7U5XXRnnpGKskMNJ0qBId8EhUayxNl1ZnLlHh1WSPobTIXOix_PvWlux1waSzxZ8ZOvFwfiNkgxPk2pFcjazh0xF"
                ),
                SignatureItem(
                    name = "The Big Baik",
                    price = 349,
                    tag = "Premium Choice",
                    desc = "Premium chicken breast fillet with melted cheddar, spicy aioli, and artisanal brioche bun.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA92X2aEWt7cikq0Lv8pOJnU7q9WvHnFRqcM-cY6api_yMZec-UctaxQHMD_X6ft_q5KzepqbYmM5ongKbNnde7OO3r0qoLlfPXMcrxBqqLbsWAQA4wQEbp7GScKjGcVLWu02UbrsWKPzVvDcVgDARbe3VEIG0Qs3_tKMJ4sorq3tO7CMf2fjztXMNmQDRqxR-GCwWCpIzSJUZ1Ge7ACXL_e4xpmUSY2K7QHl5-RU9OEygNaykq4U8b6upORaNb5TkxM2Xo1lSxhrGH"
                ),
                SignatureItem(
                    name = "Spicy Zinger Roll",
                    price = 289,
                    tag = "New Launch",
                    desc = "Shredded crispy chicken tossed in sriracha-garlic sauce, wrapped in a buttery paratha.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA5oJEL5AY7od_v0KvSjlEBPAySUdMD-S3R3tSvASpMclXlHQxoPOGxQJOujr3QOKGDWQFkM633wnJhtQAE23VtZ4rs4oDbItI522lt0bI99qZZrjgwIB6UzPsk45N6jJAs40cEBwtS4WpsJ7IaJz5Ctan3eGqZ-hbeSdTlMBeEh3ogXq5FOr9ggjJOrq3Gg8HtY-P8gT_WiscOenxbrrmdyzZ7lUS38LxNaudiqdvAIUVgS9VtZOgzfqjT8CaJmI4bPZeDiiQUHD6r"
                )
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(signatureItems) { item ->
                    Card(
                        modifier = Modifier
                            .width(320.dp)
                            .height(440.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x771E1E1E)),
                        border = BorderStroke(1.dp, Color(0x1AFFFFFF)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = item.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                // Tag
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(12.dp)
                                        .background(
                                            if (item.tag == "Best Seller") SecondaryGold else PrimaryCrimson,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = item.tag,
                                        color = if (item.tag == "Best Seller") Color.Black else Color.White,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.name,
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "₹${item.price}",
                                            color = SecondaryGold,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = item.desc,
                                        color = Color(0xFFE6BDBB),
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 3
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.addToCart(
                                            name = item.name,
                                            price = item.price,
                                            isVeg = false,
                                            imageUrl = item.imageUrl
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                ) {
                                    Text(
                                        "Add to Cart",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Sensory Experience Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF131313))
                    .border(BorderStroke(1.dp, Color(0x0FFFFFFF)))
                    .padding(vertical = 48.dp, horizontal = 24.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "A Sensory Masterpiece",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "We've transformed the fast-food experience into a luxury affair. From our curated Srinagar locations to the precision-temperature frying, every detail is engineered for the ultimate crunch.",
                        color = Color(0xFFE6BDBB),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "18+",
                                color = PrimaryCrimson,
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Secret Spices",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "100%",
                                color = SecondaryGold,
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Fresh Chicken",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Image and quote panel
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(24.dp))
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuC1FIgbOq8TARIpBXVa5p4qpN3sb4xZKn3hmn4VFla12PXwNFLOZ3ajYEU-uKzDPNiG_8HNViA1PQyvsc7WP6bXjbKeZJGwtyZdK1Op3i996OaFFogOEAWpYWNQcn7Wxj6kO_F14JQkonaTgGl1ZCb-QTGHPpURCBW1xy98oxJAjvOW067x2K68aovNJ1r0CzTGA10Umy9RNY3Am1bs9ipFNJj9IUxBqPSYlL-2bQJhS3y9wYYp4mMBMu8T7-vcBzv8ULcYYv2Ywbjh",
                            contentDescription = "Al Baik Srinagar bucket reflecting red glow",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                        )
                        // Floating Glass Quote Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xBB111111)),
                            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                                .fillMaxWidth(0.9f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "\"The crunch heard across Kashmir.\"",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Footer Info
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "© 2026 AL BAIK Srinagar. Premium Fast Casual Dining.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Green, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Currently Serving Lal Chowk & Rajbagh",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp)) // Padding for bottom navbar
        }

        // WhatsApp Floating CTA
        FloatingActionButton(
            onClick = { /* Open WhatsApp order link */ },
            containerColor = Color(0xFF25D366),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 92.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Message,
                    contentDescription = "WhatsApp Order"
                )
                Text(
                    "Order on WhatsApp",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

data class SignatureItem(
    val name: String,
    val price: Int,
    val tag: String,
    val desc: String,
    val imageUrl: String
)
