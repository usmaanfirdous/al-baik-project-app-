package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.SecondaryGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchesScreen() {
    val scrollState = rememberScrollState()

    val branches = listOf(
        BranchData(
            name = "Rajbagh Elite",
            address = "Opposite Riverfront Mall, Rajbagh, Srinagar",
            hours = "11:00 AM - 11:00 PM",
            phone = "+91 194 40221",
            status = "Open Now",
            xOffsetPercent = 0.4f,
            yOffsetPercent = 0.6f
        ),
        BranchData(
            name = "Lal Chowk (Clock Tower)",
            address = "Main Square, Near Historic Ghanta Ghar, Lal Chowk, Srinagar",
            hours = "11:00 AM - 10:30 PM",
            phone = "+91 194 40222",
            status = "Open Now",
            xOffsetPercent = 0.45f,
            yOffsetPercent = 0.45f
        ),
        BranchData(
            name = "Hyderpora Bypass",
            address = "Main Airport Road Crossing, Hyderpora, Srinagar",
            hours = "12:00 PM - 11:00 PM",
            phone = "+91 194 40223",
            status = "Open Now",
            xOffsetPercent = 0.3f,
            yOffsetPercent = 0.7f
        )
    )

    var selectedBranch by remember { mutableStateOf(branches[0]) }

    // Contact form fields
    var contactName by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }
    var contactSubject by remember { mutableStateOf("General Inquiry") }
    var contactMessage by remember { mutableStateOf("") }
    var showFormSuccess by remember { mutableStateOf(false) }

    // FAQs list
    val faqs = listOf(
        FaqItem(
            q = "Do you deliver to all areas in Srinagar?",
            a = "Yes! We offer direct home delivery within an 8km radius of all our branches. This fully covers Rajbagh, Lal Chowk, Hyderpora, Jawahar Nagar, Hazratbal, Soura, and Sanat Nagar."
        ),
        FaqItem(
            q = "What is the secret behind the Al Baik crunch?",
            a = "Our legendary crunch is achieved through precision-pressure frying of 100% fresh, non-frozen chicken, marinated for 18 hours in our 18+ secret spice formula."
        ),
        FaqItem(
            q = "Can I request franchise or bulk catering bookings?",
            a = "Absolutely! For bulk catering or private lounge reservations, please use the contact form below or reach us directly at bulk@albaik-srinagar.com."
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuA1oNhGWK8UwpeWn4gfxhR5ll-KBU63wzC9rN016jpxevPaI4YujUyO2NBVzlaODb5Bcv6ELnRyHuSR79RuwLSl2xUXNF7fhcN8BO5kUrFS7lhLpDJYXgn1POe1UJZ704wY3lnfSd47PAufNXIvlDcnZy4afu0v4S8uqPe7wAb2045zDx-7-1QtFOP4wG0jgyjINB6QN9_uqc7xXvMb06mFDf9M_dAqXoMf-1MnefVdsq2uSuLcDEbUCNV3ONLXnhtZ9W-QL6S7jNAu",
                    contentDescription = "Srinagar Branches map",
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
                        text = "FIND US NEAR YOU",
                        color = SecondaryGold,
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Srinagar Hubs",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Branch List Selector
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                branches.forEach { branch ->
                    val isSelected = selectedBranch.name == branch.name
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { selectedBranch = branch },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF1E1415) else Color(0xFF131313)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) PrimaryCrimson.copy(alpha = 0.5f) else Color(0x1AFFFFFF)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icon Badge
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        if (isSelected) PrimaryCrimson else Color(0xFF222222),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Pin",
                                    tint = if (isSelected) Color.White else SecondaryGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = branch.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF2E7D32), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = branch.status,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = branch.address,
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Schedule,
                                            contentDescription = "Hours",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(branch.hours, color = Color.Gray, fontSize = 12.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Phone,
                                            contentDescription = "Phone",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(branch.phone, color = Color.Gray, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Map Area Title
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Live Digital Map",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Glow pins indicate active kitchen servers in Srinagar.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Map Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(24.dp))
            ) {
                // Spheroid grid / map texture backdrop using canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0F0F0F))
                ) {
                    val w = size.width
                    val h = size.height

                    // Drawing futuristic glowing grid lines
                    val gridSpacing = 40.dp.toPx()
                    var x = 0f
                    while (x < w) {
                        drawLine(
                            color = Color(0x0CFF0000),
                            start = Offset(x, 0f),
                            end = Offset(x, h),
                            strokeWidth = 1f
                        )
                        x += gridSpacing
                    }

                    var y = 0f
                    while (y < h) {
                        drawLine(
                            color = Color(0x0CFF0000),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                        y += gridSpacing
                    }

                    // Draw abstract roads/river curves
                    drawArc(
                        color = Color(0x11E31837),
                        startAngle = 10f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(w * 0.1f, h * 0.2f),
                        size = size * 0.8f,
                        style = Stroke(width = 8f)
                    )

                    drawArc(
                        color = Color(0x0A00E5FF), // Jhelum River glow blue
                        startAngle = 45f,
                        sweepAngle = 100f,
                        useCenter = false,
                        topLeft = Offset(w * 0.3f, -h * 0.2f),
                        size = size * 1.2f,
                        style = Stroke(width = 16f)
                    )
                }

                // Dynamic Markers representing branches
                branches.forEach { branch ->
                    val isSelected = selectedBranch.name == branch.name

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Position based on percentage
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(
                                    x = ((branch.xOffsetPercent - 0.5f) * 240).dp,
                                    y = ((branch.yOffsetPercent - 0.5f) * 200).dp
                                )
                                .size(if (isSelected) 36.dp else 24.dp)
                                .background(
                                    if (isSelected) PrimaryCrimson.copy(alpha = 0.3f) else Color.Transparent,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Pulsing core pin
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        if (isSelected) PrimaryCrimson else SecondaryGold,
                                        CircleShape
                                    )
                                    .border(BorderStroke(2.dp, Color.White), CircleShape)
                            )
                        }
                    }
                }

                // Selected Branch Overlay Badge
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xDD111111)),
                    border = BorderStroke(1.dp, Color(0x22FFFFFF)),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = selectedBranch.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Primary Hotline: ${selectedBranch.phone}",
                            color = SecondaryGold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Get in Touch Contact Form
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(Color(0xFF131313), RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Get in Touch",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Request catering lounges or report ordering inquiries.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (!showFormSuccess) {
                        OutlinedTextField(
                            value = contactName,
                            onValueChange = { contactName = it },
                            label = { Text("Full Name", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryCrimson,
                                unfocusedBorderColor = Color(0x1AFFFFFF),
                                focusedContainerColor = Color(0xFF0F0F0F),
                                unfocusedContainerColor = Color(0xFF0F0F0F)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = contactEmail,
                            onValueChange = { contactEmail = it },
                            label = { Text("Email Address", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryCrimson,
                                unfocusedBorderColor = Color(0x1AFFFFFF),
                                focusedContainerColor = Color(0xFF0F0F0F),
                                unfocusedContainerColor = Color(0xFF0F0F0F)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Subject Dropdown Mock Select
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F0F0F), RoundedCornerShape(10.dp))
                                .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(10.dp))
                                .clickable {
                                    contactSubject = when (contactSubject) {
                                        "General Inquiry" -> "Bulk Table Booking"
                                        "Bulk Table Booking" -> "Special Event Catering"
                                        else -> "General Inquiry"
                                    }
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Subject: $contactSubject",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select",
                                tint = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = contactMessage,
                            onValueChange = { contactMessage = it },
                            label = { Text("Message details", color = Color.Gray) },
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryCrimson,
                                unfocusedBorderColor = Color(0x1AFFFFFF),
                                focusedContainerColor = Color(0xFF0F0F0F),
                                unfocusedContainerColor = Color(0xFF0F0F0F)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (contactName.isNotBlank() && contactEmail.isNotBlank()) {
                                    showFormSuccess = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                "Submit Message",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    } else {
                        // Sent successful state
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(Color(0xFF4CAF50).copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Sent",
                                    tint = Color(0xFF4CAF50)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Message Dispatched Successfully!",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Thank you $contactName. Our Srinagar guest relations executive will review and email you back within 2 hours.",
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            OutlinedButton(
                                onClick = {
                                    showFormSuccess = false
                                    contactName = ""
                                    contactEmail = ""
                                    contactMessage = ""
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Send Another", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Expandable FAQs Panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Location FAQs",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                faqs.forEach { faq ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { isExpanded = !isExpanded },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131313)),
                        border = BorderStroke(1.dp, Color(0x0FFFFFFF))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = faq.q,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(0.9f)
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = "Expand",
                                    tint = Color.Gray
                                )
                            }

                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = faq.a,
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp)) // Padding for bottom navbar
        }
    }
}

data class BranchData(
    val name: String,
    val address: String,
    val hours: String,
    val phone: String,
    val status: String,
    val xOffsetPercent: Float,
    val yOffsetPercent: Float
)

data class FaqItem(
    val q: String,
    val a: String
)
