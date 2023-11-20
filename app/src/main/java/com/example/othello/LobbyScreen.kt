package com.example.othello

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.garrit.android.multiplayer.Game
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.ServerState
import io.garrit.android.multiplayer.SupabaseService
import kotlinx.coroutines.ExperimentalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun LobbyScreen(navController: NavController) {
    val serverState = SupabaseService.serverState.collectAsState()
    var playerName by remember { mutableStateOf("") }
    //val users = SupabaseService.users.collectAsState() //PÅ DETTA SÄTT?

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Enter your name") }
            )

            Button(
                onClick = {
                    navController.navigate(Screen.Game.route) // Call the callback to navigate to GameScreen
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Start Game")
            }
            when (serverState.value) {
                ServerState.NOT_CONNECTED -> {
                    // Fix not connected state
                }

                ServerState.LOADING_LOBBY -> {
                    // Fix loading lobby state
                }

                ServerState.LOBBY -> {
                    // LobbyContent(/*users.value, games.value, viewModel*/)
                }

                ServerState.LOADING_GAME -> {
                    // Fix loading game state
                }

                ServerState.GAME -> {
                    // Fix game state
                }
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun LobbyContent(
   // users: List<Player>, vänta med denna
    viewModel: LobbyViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display list of users in the lobby
       /* Text("Users in the Lobby:") Vänta med denna
        users.forEach { user ->
            Text(user.name)
        } */

        Spacer(modifier = Modifier.height(16.dp))


    }
}