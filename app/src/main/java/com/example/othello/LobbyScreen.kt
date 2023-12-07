import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.othello.LobbyViewModel
import com.example.othello.R
import com.example.othello.Screen
import io.garrit.android.multiplayer.Game
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.ServerState
import io.garrit.android.multiplayer.SupabaseService
import io.garrit.android.multiplayer.SupabaseService.serverState


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LobbyScreen(navController: NavController, viewModel: LobbyViewModel, isDarkMode: Boolean) {
    val backgroundGradient = if (isDarkMode) {
        Brush.verticalGradient(colors = listOf(Color(0xFF232526), Color(0xFF414345)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC)))
    }
    Surface(
        modifier = Modifier.fillMaxSize()
            .background(backgroundGradient),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OTHELLO LOBBY",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = if (isDarkMode) Color.White else Color.DarkGray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Players Online",

                style=TextStyle(fontSize = 22.sp,
                    color = if (isDarkMode) Color.White else Color.DarkGray),
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(
                modifier = Modifier
                    .border(
                        width = 6.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .width(280.dp)
                    .height(180.dp)
            ) {
                items(SupabaseService.users) { player ->
                    onlinePlayers(player = player, viewModel = viewModel, isDarkMode = isDarkMode)

                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your Challenges",
                style=TextStyle(
                    fontSize = 22.sp,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier,
                color = if (isDarkMode) Color.White else Color.DarkGray
            )
            Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .border(
                        width = 6.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .width(280.dp)
                    .height(180.dp)
            ) {
                items(SupabaseService.games) { game ->
                    challenges(navController, game, viewModel)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            when (serverState.collectAsState().value) {
                ServerState.NOT_CONNECTED -> Text("Not Connected")
                ServerState.LOADING_LOBBY -> CircularProgressIndicator()
                ServerState.LOBBY -> Text("In the lobby...",
                    color = if (isDarkMode) Color.White else Color.DarkGray
                )
                ServerState.LOADING_GAME -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("Loading Game...",color = if (isDarkMode) Color.White else Color.DarkGray
                    )
                }
                ServerState.GAME -> LaunchedEffect(key1 = Unit) {
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.Lobby.route) { inclusive = true }
                    }
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
}

@Composable
fun onlinePlayers(player: Player, viewModel: LobbyViewModel, isDarkMode: Boolean) {
    val currentUser = SupabaseService.player

    // Check if the player is not the current user
    if (currentUser != null && player.id != currentUser.id) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp,)
        ) {
            Text(
                text = player.name,
                color = if (isDarkMode) Color.White else Color.DarkGray,
                modifier = Modifier
                    .width(150.dp)
                    .padding(end = 10.dp)
            )

            Button(
                onClick = {
                    viewModel.sendGameInvitation(player)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD9D9D9)
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(25.dp)
                    .padding(start = 20.dp)
            ) {
                Text(
                    "Challenge",
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun challenges(navController: NavController, player: Game, viewModel: LobbyViewModel) {

    Column() {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                ) {
                    append(player.player1.name + " challenged you")
                }

            },
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            Button(
                onClick = {
                    viewModel.acceptGameInvitation(player)
                    navController.navigate(Screen.Game.route)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
                    .width(110.dp)
                    .height(35.dp)
            ) {
                Text(
                    text = "Accept Game",
                    textAlign = TextAlign.Center

                )
            }


            Button(
                onClick = {
                    viewModel.declineGameInvitation(player)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
                    .width(110.dp)
                    .height(35.dp)
            ) {
                Text(
                    text = "Decline Game",
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}