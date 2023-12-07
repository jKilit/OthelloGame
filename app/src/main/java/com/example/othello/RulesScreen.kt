package com.example.othello

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RulesScreen(isDarkMode: Boolean) {
    val backgroundBrush = Brush.verticalGradient(
        colors = if (isDarkMode)
            listOf(Color(0xFF232526), Color(0xFF414345))
        else
            listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC))
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Decorative Image
            Image(
                painter = painterResource(id = R.drawable.rulesbook),
                contentDescription = "Rules Book",
                modifier = Modifier
                    .size(220.dp)
                    .padding(60.dp)
            )

            Text(
                text = "Othello Rules",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Instructions:\n" +
                            "1. Black starts the game.\n" +
                            "2. Players take turns placing their color on an empty cell.\n" +
                            "3. To capture opponent's pieces, sandwich them between your own.\n" +
                            "4. The game ends when the board is full or no more moves can be made.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 18.sp,
                        color = if (isDarkMode) Color.White else Color.Black
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.othello),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "© 2023 DOBOJxKELLEF. All Rights Reserved.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isDarkMode) Color.White else Color.Black,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
