package com.example.othello

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController



@Composable
fun GameScreen(
    navController: NavController,
    viewModel: OthelloViewModel = viewModel(),
    isDarkMode: Boolean
) {
    val gameBoard = viewModel.boardState

    val gameBoardBackgroundGradient = if (isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF232526), Color(0xFF414345))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C3FC))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.Black else Color.White)
            .padding(10.dp)
            .padding(top = 40.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if(!viewModel.checkIsGameOver()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(gameBoardBackgroundGradient)
                    .padding(6.dp)
            ) {
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
            }
        }
        else{
            Image(
                painter = painterResource(id = R.drawable.gameover),
                contentDescription = null,
                modifier = Modifier
                    .size(350.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Button(onClick = {
                navController.navigate(Screen.Start.route)
            }) {
                Text("Back to Home")
            }
        }

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(gameBoardBackgroundGradient)
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    if(!viewModel.checkIsGameOver()) {
                        val (blackScore, whiteScore) = viewModel.getScores()
                        Text("Black: $blackScore  White: $whiteScore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) Color.White else Color.DarkGray)
                    }
                    else{
                        Text("GAME IS OVER")
                    }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val turnText = if (viewModel.isBlackTurn) "Black's Turn" else "White's Turn"
                    if (!viewModel.checkIsGameOver()) {
                        Text(
                            turnText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.White else Color.DarkGray
                        )
                    } else {
                        val (blackScore, whiteScore) = viewModel.getScores()
                        Column {
                            Text("Result: ${viewModel.finalStatus}")
                        }
                        Column{
                            Text("  | Scores: Black:$blackScore, White:$whiteScore")
                        }
                    }
                    if (!viewModel.checkIsGameOver()) {
                        if (viewModel.isYourTurn) {
                            Text(
                                " (Your Turn)",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkMode) Color.Cyan else Color.Blue
                            )
                        }
                    }
                }
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
