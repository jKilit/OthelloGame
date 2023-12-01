package com.example.othello

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun GameScreen(navController: NavController, viewModel: OthelloViewModel = viewModel(), isDarkMode: Boolean) {
    val gameBoard = viewModel.boardState
    val isFirstMove = viewModel.getScores() == Pair(2, 2) // Assuming this is how you determine the first move

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.Black else Color.White)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isFirstMove && viewModel.isBlackTurn) {
                Text(
                    text = "Your Turn (Black)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isDarkMode) Color.DarkGray else Color.LightGray)
                .padding(6.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(OthelloViewModel.BOARD_SIZE),
                modifier = Modifier.fillMaxSize()
            ) {
                items(gameBoard) { tile ->
                    TileView(tile = tile, onClick = {
                        viewModel.makeMove(tile.x, tile.y, navController)
                    })
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isDarkMode) Color.DarkGray else Color.LightGray)
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Displaying scores
                val (blackScore, whiteScore) = viewModel.getScores()
                Text(
                    "Black: $blackScore  White: $whiteScore",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                val turnText = if (viewModel.isBlackTurn) "Black's Turn" else "White's Turn"
                Text(
                    turnText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun TileView(tile: Tile, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .padding(2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                tile.isBlack -> Color.Black
                tile.isWhite -> Color.White
                else -> Color.Gray
            }
        )
    ) {
        // Empty for now
    }
}
