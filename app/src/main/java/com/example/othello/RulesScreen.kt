package com.example.othello

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RulesScreen(isDarkMode: Boolean) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (isDarkMode) Color.DarkGray else MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Decorative Image
            Image(
                painter = painterResource(id = R.drawable.rulesbook),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.padding(8.dp))


            Text(
                text = "Othello Rules",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )


            Text(
                text = "Instructions:\n" +
                        "1. Black starts the game.\n" +
                        "2. Players take turns placing their color on an empty cell.\n" +
                        "3. To capture opponent's pieces, sandwich them between your own.\n" +
                        "4. The game ends when the board is full or no more moves can be made.",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
