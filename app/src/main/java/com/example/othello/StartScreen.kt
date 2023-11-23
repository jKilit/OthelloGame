import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.othello.LobbyViewModel
import com.example.othello.R
import com.example.othello.Screen
import io.garrit.android.multiplayer.Player

@Composable
fun StartScreen(navController: NavController, viewModel: LobbyViewModel, isDarkMode: Boolean) {
    var username by remember {
        mutableStateOf("")
    }
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

            Text(
                text = "Instructions:\n1. Black starts the game.\n2. Players take turns placing their color on an empty cell.\n3. To capture opponent's pieces, sandwich them between your own.\n4. The game ends when the board is full or no more moves can be made.",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = "Enter your username",
            )
            Spacer(modifier = Modifier.height(10.dp))

            var username by remember { mutableStateOf("") }
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

            Button(
                onClick = {
                    viewModel.joinLobby(Player(name = username))
                    navController.navigate(Screen.Lobby.route)
                },

            ) {
                Text(
                    "Join Lobby",
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                navController.navigate(Screen.Settings.route)
            }) {
                Text("Settings")
            }

            Text(
                text = "About Othello:\nOthello is a classic strategy board game for two players. It's also known as Reversi. We hope you will enjoy it!",
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
