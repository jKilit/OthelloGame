package com.example.othello


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(viewModel: OthelloViewModel = viewModel()) {
    val gameBoard = viewModel.boardState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(OthelloViewModel.BOARD_SIZE),
            modifier = Modifier.fillMaxSize()
        ) {
            items(gameBoard) { tile ->
                TileView(tile = tile, onClick = {
                    viewModel.makeMove(tile.x, tile.y)
                })
            }
        }

        // Displaying scores
        val (blackScore, whiteScore) = viewModel.getScores()
        Text("Black: $blackScore, White: $whiteScore")
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

