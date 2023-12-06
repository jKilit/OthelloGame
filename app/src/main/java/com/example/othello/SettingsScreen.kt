package com.example.othello

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.othello.R
import com.example.othello.Screen

@Composable
fun SettingsScreen(navController: NavController, isDarkMode: Boolean , setDarkMode: (Boolean) -> Unit) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (isDarkMode)  Color(0xFF232526)else MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp)
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
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isChecked ->
                        setDarkMode(isChecked)
                    }
                )
            }
        }
    }
}
