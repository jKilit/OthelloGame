package com.example.othello

import androidx.compose.runtime.Composable

data class Tile(
    val x: Int,
    val y: Int,
    var isBlack: Boolean,
    var isWhite: Boolean
)

fun flip(tile: Tile) {

    if (tile.isBlack) {
        tile.isBlack = false
        tile.isWhite = true
    } else {
        tile.isBlack = true
        tile.isWhite = false
    }
}
@Composable
fun putBlack(tile: Tile){
    tile.isBlack=true
    tile.isWhite=false

}
@Composable
fun putWhite(tile:Tile){
    tile.isWhite=true
    tile.isBlack=false

}

class OthelloViewModel {

}
