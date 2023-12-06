package com.example.othello

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.othello.LobbyViewModel
import io.garrit.android.multiplayer.Player

@Composable
fun StartScreen(navController: NavController, viewModel: LobbyViewModel, isDarkMode: Boolean) {
    var username by remember { mutableStateOf("") }

    // Define a gradient background
    val lightBackgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC))
    )
    val darkBackgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF232526), Color(0xFF414345))
    )
    val background = if (isDarkMode) darkBackgroundGradient else lightBackgroundGradient

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (isDarkMode) Color.DarkGray else MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.othello), // Consider updating the logo for a modern look
                    contentDescription = null,
                    modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Welcome to Othello",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) Color.White else Color.Black
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Enter your username") },
                    singleLine = true,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Validation Button with new style
                ElevatedButton(
                    onClick = {
                        viewModel.joinLobby(Player(name = username))
                        navController.navigate(Screen.Lobby.route)
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Join Lobby",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Settings Button
                OutlinedButton(
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text("Settings", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Game Description Text
                Text(
                    text = "About Othello:\nOthello is a classic strategy board game for two players. It's also known as Reversi. We hope you will enjoy it!",
                    color = if (isDarkMode) Color.White else Color.Black,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Rules Button
                Button(
                    onClick = {
                        navController.navigate(Screen.Rules.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text("View Rules", fontSize = 18.sp)
                }
            }
        }
    }
}
