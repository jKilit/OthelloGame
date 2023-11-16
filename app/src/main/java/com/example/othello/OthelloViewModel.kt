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
                makeBlack(selectedTile)
            } else {
                makeWhite(selectedTile)
            }
            updateBoardState()//After flip?
            flipTiles(x, y)
            isBlackTurn = !isBlackTurn  // Switch turn after a valid move
        }
    }

    // Inside OthelloViewModel
    fun isValidMove(tile: Tile): Boolean {
        if (!tile.isEmpty()) {
            return false // Cannot place a tile on an already occupied position
        }

        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) continue // Skip the current tile

                val adjacentTile = getTile(tile.x + i, tile.y + j)

                if (isValidDirection(tile, adjacentTile, i, j)) {
                    return true
                }
            }
        }

        return false
    }

    private fun isValidDirection(tile: Tile, adjacentTile: Tile, deltaX: Int, deltaY: Int): Boolean {
        // Check if the adjacent tile is of the opposite color
        if ((isBlackTurn && adjacentTile.isWhite) || (!isBlackTurn && adjacentTile.isBlack)) {
            var x = tile.x + deltaX
            var y = tile.y + deltaY

            while (x in 0 until BOARD_SIZE && y in 0 until BOARD_SIZE) {
                val currentTile = getTile(x, y)

                if (currentTile.isEmpty()) {
                    return false // Empty tile, invalid move
                } else if ((isBlackTurn && currentTile.isBlack) || (!isBlackTurn && currentTile.isWhite)) {
                    return true // Found a valid sequence to flip
                }

                x += deltaX
                y += deltaY
            }
        }

        return false
    }


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
        val currentTile = getTile(x, y)

        // Check to the left
        var leftFlippable = false
        var leftX = x - 1
        while (leftX >= 0 && getTile(leftX, y).isWhite != currentTile.isWhite) {//checks if colors are different
            if (getTile(leftX, y).isEmpty()) { //https://chat.openai.com/share/b99130fd-406e-493e-8df0-847ae1bdb512
                leftFlippable = true
                break
            }
            leftX--
        }

        // Check to the right
        var rightFlippable = false
        var rightX = x + 1
        while (rightX < BOARD_SIZE && getTile(rightX, y).isWhite != currentTile.isWhite) {
            if (getTile(rightX, y).isEmpty()) {
                rightFlippable = true
                break
            }
            rightX++
        }

        return leftFlippable || rightFlippable
    }


    private fun hasFlippableTilesVertical(x: Int, y: Int): Boolean {
        // Add your logic to check for flippable tiles vertically
        return true
    }

    private fun hasFlippableTilesDiagonal(x: Int, y: Int): Boolean {
        val currentTile = getTile(x, y)

        var topLeftFlippable = false
        var diagX = x - 1
        var diagY = y - 1
        while (diagX >= 0 && diagY >= 0 && getTile(diagX, diagY).isWhite != currentTile.isWhite) {
            if (getTile(diagX, diagY).isEmpty()) {
                topLeftFlippable = true
                break
            }
            diagX--
            diagY--
        }
        // do these
        var topRightFlippable = false
        var bottomLeftFlippable = false
        var bottomRightFlippable = false

        return topLeftFlippable || topRightFlippable || bottomLeftFlippable || bottomRightFlippable
    }


    // Get the tile at a specific position
    private fun getTile(x: Int, y: Int): Tile {
        return boardState[x * BOARD_SIZE + y]
    }

    private fun updateBoardState() {
        boardState.clear()
        boardState.addAll(gameBoard.flatten())
    }

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
