package com.example.othello

import androidx.compose.runtime.Composable


data class Tile(
    val x: Int,
    val y: Int,
    val isBlack: Boolean,
    val isWhite: Boolean
)

@Composable
fun flip(tile: Tile){
    if (tile.isBlack){
        tile.isBlack=false
        tile.isWhite=true
        return tile;
    }
}


class OthelloViewModel {

}