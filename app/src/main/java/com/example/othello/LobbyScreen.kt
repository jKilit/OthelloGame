import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
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
import com.example.othello.Screen
import io.garrit.android.multiplayer.Game
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.ServerState
import io.garrit.android.multiplayer.SupabaseService
import io.garrit.android.multiplayer.SupabaseService.serverState


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LobbyScreen(navController: NavController, viewModel: LobbyViewModel, isDarkMode: Boolean) {
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
            Text(
                text = "OTHELLO LOBBY",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Players Online",
                style=TextStyle(
                    fontSize = 30.sp,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(
                modifier = Modifier
                    .border(
                        width = 6.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .width(280.dp)
                    .height(140.dp)
            ) {
                items(SupabaseService.users) { player ->
                    onlinePlayers(player, viewModel)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your Challenges",
                style=TextStyle(
                    fontSize = 30.sp,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .border(
                        width = 6.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .width(280.dp)
                    .height(140.dp)
            ) {
                items(SupabaseService.games) { game ->
                    challenges(navController, game, viewModel)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate(Screen.Game.route) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Start Game")
            }

            when (serverState.collectAsState().value) {
                ServerState.NOT_CONNECTED -> Text("Not Connected")
                ServerState.LOADING_LOBBY -> CircularProgressIndicator()
                ServerState.LOBBY -> Text("In the lobby...")
                ServerState.LOADING_GAME -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("Loading Game...")
                }
                ServerState.GAME -> LaunchedEffect(key1 = Unit) {
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.Lobby.route) { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun onlinePlayers(player: Player, viewModel: LobbyViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp,)
    ) {
        Text(
            text = player.name,
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun challenges(navController: NavController, player: Game, viewModel: LobbyViewModel) {

    Column() {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                ) {
                    append(player.player1.name)
                }
                append("\n") //break

                withStyle(
                    style = SpanStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                ) {
                    append("has challenged you")
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
                    .width(60.dp)
                    .height(25.dp) //gör större
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
                    .width(60.dp)
                    .height(25.dp)
            ) {
                Text(
                    text = "Decline Game",
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}








