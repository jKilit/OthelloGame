package com.example.othello

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun Validation(text: String, buttonTitle: String, onValidInput: () -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = {
            if (text.length < 3) {
                Toast.makeText(context, "Title more than 3 characters", Toast.LENGTH_LONG).show()
            } else if (text.length > 10) {
                Toast.makeText(context, "Text less than 10 characters", Toast.LENGTH_LONG).show()
            } else {
                onValidInput()
            }
        }
    ) {
        Text(text = buttonTitle)
    }
}

@Composable
fun StartScreen(navController: NavController, viewModel: LobbyViewModel, isDarkMode: Boolean) {
    var username by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (isDarkMode) Color.DarkGray else MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.othello),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Text(
                text = "Welcome to Othello",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = "Enter your username",
                color = if (isDarkMode) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.DarkGray,)
            ) {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Validation(
                text = username,
                buttonTitle = "Join Lobby",
                onValidInput = {
                    viewModel.joinLobby(Player(name = username))
                    navController.navigate(Screen.Lobby.route)
                }
            )



            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                navController.navigate(Screen.Settings.route)
            }) {
                Text("Settings")
            }

            Text(
                text = "About Othello:\nOthello is a classic strategy board game for two players. It's also known as Reversi. We hope you will enjoy it!",
                color = if (isDarkMode) Color.White else Color.Black,
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                navController.navigate(Screen.Rules.route)
            }) {
                Text("View Rules")
            }
        }
    }
}
