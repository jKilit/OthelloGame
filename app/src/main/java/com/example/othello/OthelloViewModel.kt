package com.example.othello

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Tile(
    val x: Int,
    val y: Int,
    var isBlack: Boolean,
    var isWhite: Boolean
) {
    fun isEmpty(): Boolean {
        return !isBlack && !isWhite
    }
}

fun flip(tile: Tile) {
    if (tile.isBlack) {
        tile.isBlack = false
        tile.isWhite = true
    } else {
        tile.isBlack = true
        tile.isWhite = false
    }
}

fun makeBlack(tile: Tile) {
    tile.isBlack = true
    tile.isWhite = false
}

fun makeWhite(tile: Tile) {
    tile.isWhite = true
    tile.isBlack = false
}

class OthelloViewModel : ViewModel() {

    companion object {
        const val BOARD_SIZE = 8
    }

    private val gameBoard: List<List<Tile>> = List(BOARD_SIZE) { x -> //2d lista för logik
        List(BOARD_SIZE) { y ->
            Tile(x, y, isBlack = false, isWhite = false)
        }
    }

    // MutableStateList is used to observe changes to the list in Compose
     val boardState = mutableStateListOf<Tile>().apply {//skapar en 1d lista av 2d gameboard och lägger den i boardstate
        addAll(gameBoard.flatten())
    }

    private var isBlackTurn = true

    init { //initziering block, koden körs när othello classen skapas
        // Initialize the starting position of Othello
        putPiece(3, 3, true)
        putPiece(3, 4, false)
        putPiece(4, 3, false)
        putPiece(4, 4, true)
    }

    // Function to handle a move
    fun makeMove(x: Int, y: Int) {
        val selectedTile = getTile(x, y)

        if (isValidMove(selectedTile)) {
            if (isBlackTurn) {
                putBlack(selectedTile)
            } else {
                putWhite(selectedTile)
            }
            flipTiles(x, y)
            isBlackTurn = !isBlackTurn  // Switch turn after a valid move
        }
    }

    //private fun isValidMove(tile: Tile): Boolean {
    //}

    // Place a piece on the board
    private fun putPiece(x: Int, y: Int, isBlack: Boolean) {
        val tile = getTile(x, y)
        if (isBlack) {
            makeBlack(tile)
        } else {
            makeWhite(tile)
        }
    }

    // Flip tiles based on the placed piece
    private fun flipTiles(x: Int, y: Int) {
        flipHorizontal(x, y)
        flipVertical(x, y)
        flipDiagonal(x, y)
    }

    private fun flipHorizontal(x: Int, y: Int) {
        // Add your logic to flip tiles horizontally
    }

    private fun flipVertical(x: Int, y: Int) {
        // Add your logic to flip tiles vertically
    }

    private fun flipDiagonal(x: Int, y: Int) {
        // Add your logic to flip tiles diagonally
    }

    // Check if there are flippable tiles in any direction
    private fun hasFlippableTiles(x: Int, y: Int): Boolean {
        return hasFlippableTilesHorizontal(x, y) ||
                hasFlippableTilesVertical(x, y) ||
                hasFlippableTilesDiagonal(x, y)
    }

    private fun hasFlippableTilesHorizontal(x: Int, y: Int): Boolean {
        // Add your logic to check for flippable tiles horizontally
        return true
    }

    private fun hasFlippableTilesVertical(x: Int, y: Int): Boolean {
        // Add your logic to check for flippable tiles vertically
        return true
    }

    private fun hasFlippableTilesDiagonal(x: Int, y: Int): Boolean {
        // Add your logic to check for flippable tiles diagonally
        return true
    }

    // Get the tile at a specific position
    private fun getTile(x: Int, y: Int): Tile {
        return boardState[y * BOARD_SIZE + x]
    }

    // Get the current state of the game board
    fun getGameBoard(): List<List<Tile>> {
        return boardState.chunked(BOARD_SIZE)
    }

    // Get the current scores
// Get the current scores
// Get the current scores
    fun getScores(): Pair<Int, Int> {
        var blackScore = 0
        var whiteScore = 0

        for (tile in boardState) {
            if (tile.isBlack) {
                blackScore++
            } else if (tile.isWhite) {
                whiteScore++
            }
        }

        return Pair(blackScore, whiteScore)
    }

}
