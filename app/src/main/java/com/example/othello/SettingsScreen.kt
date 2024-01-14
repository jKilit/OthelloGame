package com.example.othello

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.othello.R
import com.example.othello.Screen


@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkMode: Boolean,
    setDarkMode: (Boolean) -> Unit
) {
    val lightBackgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC))
    )
    val darkBackgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF232526), Color(0xFF414345))
    )
    var background = if (isDarkMode) darkBackgroundGradient else lightBackgroundGradient

    Surface(
        modifier = Modifier.fillMaxSize(),

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)

        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(16.dp)
                    .padding(top = 40.dp)

            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isDarkMode) "Change to Light Mode" else "Change to Dark Mode",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = if (isDarkMode) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                )

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isChecked ->
                        setDarkMode(isChecked)
                    }
                )
            }
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
    }
}