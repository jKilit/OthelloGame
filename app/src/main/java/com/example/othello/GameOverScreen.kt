package com.example.othello

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun GameOverScreen(
    viewModel: OthelloViewModel = viewModel(),
    winner: String,
    blackScore: String,
    whiteScore: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Over!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Winner: $winner",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )

        Spacer(modifier = Modifier.height(8.dp))


            Text("Scores: Black - $blackScore, White - $whiteScore", style = MaterialTheme.typography.bodyMedium)


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /*FIX BACK TO START SCREEN*/ }) {
            Text("Back to home")
        }
    }
}