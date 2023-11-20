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
import io.garrit.android.multiplayer.SupabaseService.games
import io.garrit.android.multiplayer.SupabaseService.users
import kotlinx.coroutines.ExperimentalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun LobbyScreen(navController: NavController) {
    val serverState = SupabaseService.serverState.collectAsState()
    var playerName by remember { mutableStateOf("") }
    val viewModel: LobbyViewModel = viewModel()
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
                    viewModel.joinLobby(playerName)
                }
            ) {
                Text(text = "Join Lobby")
            }
            Button(onClick = { viewModel.leaveLobby() }) {
                Text("Leave Lobby")
            }

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
                    Text("Not Connected")
                }
                ServerState.LOADING_LOBBY -> {
                    CircularProgressIndicator()
                }
                ServerState.LOBBY -> {
                    LobbyContent(users, games, viewModel)
                }
                ServerState.LOADING_GAME -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Loading Game...")
                    }
                }
                ServerState.GAME -> {
                    LaunchedEffect(key1 = Unit) {
                        navController.navigate(Screen.Game.route) {
                            popUpTo(Screen.Lobby.route) { inclusive = true }
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun LobbyContent(
    users: List<Player>,
    games: List<Game>,
    viewModel: LobbyViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Users in the Lobby:")
        users.forEach { user ->
            Text(user.name, modifier = Modifier.clickable {
                viewModel.sendGameInvitation(user)
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Available Games:")
        games.forEach { game ->
            Text("${game.player1.name} vs ${game.player2.name}", modifier = Modifier.clickable {
                viewModel.joinGame(game)
            })
        }
    }
}
