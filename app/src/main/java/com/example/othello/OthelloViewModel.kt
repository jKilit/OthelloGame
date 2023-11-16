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

    fun flip(x: Int, y: Int) { //inside viewmodel now
        val currentNote= getTile(x,y)
        if (currentNote.isBlack) {
            currentNote.isBlack = false
            currentNote.isWhite = true
        } else {
            currentNote.isBlack = true
            currentNote.isWhite = false
        }
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

    private fun flipHorizontal(x: Int, y: Int) { //chat.openai.com/share/ef8d0e59-9fbd-4c5e-817e-877ea2d07cb5
        if (hasFlippableTilesHorizontal(x, y)) {
            val currentTile = getTile(x, y)

            // Flip tiles to the left
            var leftX = x - 1
            while (leftX >= 0 && getTile(leftX, y).isWhite != currentTile.isWhite) {
                if (getTile(leftX, y).isEmpty()) {
                    break
                }
                flip(leftX, y)
                leftX--
            }

            // Flip tiles to the right
            var rightX = x + 1
            while (rightX < BOARD_SIZE && getTile(rightX, y).isWhite != currentTile.isWhite) {
                if (getTile(rightX,y).isEmpty()) {
                    break
                }
                flip(rightX, y)
                rightX++
            }
        }
    }

    private fun flipVertical(x: Int, y: Int) {
        if(hasFlippableTilesVertical(x,y)){
            val currentTile = getTile(x, y)

            // Flip tiles up
            var upY = y - 1
            while (upY >= 0 && getTile(x, upY).isWhite != currentTile.isWhite) {
                if (getTile(x, upY).isEmpty()) {
                    break
                }
                flip(x, upY)
                upY--
            }

            // Flip tiles down
            var downY = y + 1
            while (downY < BOARD_SIZE && getTile(x, downY).isWhite != currentTile.isWhite) {
                if (getTile(x,downY).isEmpty()) {
                    break
                }
                flip(x, downY)
                downY++
            }
        }
    }

    //logic based on the chat from the other flip functions
    private fun flipDiagonal(x: Int, y: Int) {
        if (hasFlippableTilesDiagonal(x, y)) {
            val currentTile = getTile(x, y)

            var topLeftX = x - 1
            var topLeftY = y - 1
            while (topLeftX >= 0 && topLeftY >= 0 && getTile(topLeftX, topLeftY).isWhite != currentTile.isWhite) {
                if (getTile(topLeftX, topLeftY).isEmpty()) {
                    break
                }
                flip(topLeftX, topLeftY)
                topLeftX--
                topLeftY--
            }

            var topRightX = x + 1
            var topRightY = y - 1
            while (topRightX < BOARD_SIZE && topRightY >= 0 && getTile(topRightX, topRightY).isWhite != currentTile.isWhite) {
                if (getTile(topRightX, topRightY).isEmpty()) {
                    break
                }
                flip(topLeftX, topLeftY)
                topRightX++
                topRightY--
            }

            var bottomLeftX = x - 1
            var bottomLeftY = y + 1
            while (bottomLeftX >= 0 && bottomLeftY < BOARD_SIZE && getTile(bottomLeftX, bottomLeftY).isWhite != currentTile.isWhite) {
                if (getTile(bottomLeftX, bottomLeftY).isEmpty()) {
                    break
                }
                flip(bottomLeftX, bottomLeftY)
                bottomLeftX--
                bottomLeftY++
            }

            var bottomRightX = x + 1
            var bottomRightY = y + 1
            while (bottomRightX < BOARD_SIZE && bottomRightY < BOARD_SIZE && getTile(bottomRightX, bottomRightY).isWhite != currentTile.isWhite) {
                if (getTile(bottomRightX, bottomRightY).isEmpty()) {
                    break
                }
                flip(bottomRightX, bottomRightY)
                bottomRightX++
                bottomRightY++
            }
        }
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


    private fun hasFlippableTilesVertical(x: Int, y: Int): Boolean { //followed horizontal as template
        val currentTile = getTile(x, y)

        var upFlippable = false
        var upY = y - 1
        while (upY >= 0 && getTile(x, upY).isWhite != currentTile.isWhite) {
            if (getTile(x, upY).isEmpty()) {
                upFlippable = true
                break
            }
            upY--
        }

        var downFlippable = false
        var downY = y + 1
        while (downY < BOARD_SIZE && getTile(x, downY).isWhite != currentTile.isWhite) {
            if (getTile(x, downY).isEmpty()) {
                downFlippable = true
                break
            }
            downY++
        }

        return upFlippable || downFlippable
    }


    //https://chat.openai.com/share/e37a834a-e229-4bd9-a741-2e4818dd9a3f
    private fun hasFlippableTilesDiagonal(x: Int, y: Int): Boolean {
        val currentTile = getTile(x, y)

        var topLeftFlippable = false
        var topLeftX = x - 1
        var topLeftY = y - 1
        while (topLeftX >= 0 && topLeftY >= 0 && getTile(topLeftX, topLeftY).isWhite != currentTile.isWhite) {
            if (getTile(topLeftX, topLeftY).isEmpty()) {
                topLeftFlippable = true
                break
            }
            topLeftX--
            topLeftY--
        }

        var topRightFlippable = false
        var topRightX = x - 1
        var topRightY = y + 1
        while (topRightX >= 0 && topRightY < BOARD_SIZE && getTile(topRightX, topRightY).isWhite != currentTile.isWhite) {
            if (getTile(topRightX, topRightY).isEmpty()) {
                topRightFlippable = true
                break
            }
            topRightX--
            topRightY++
        }

        var bottomLeftFlippable = false
        var bottomLeftX = x + 1
        var bottomLeftY = y - 1
        while (bottomLeftX < BOARD_SIZE && bottomLeftY >= 0 && getTile(bottomLeftX, bottomLeftY).isWhite != currentTile.isWhite) {
            if (getTile(bottomLeftX, bottomLeftY).isEmpty()) {
                bottomLeftFlippable = true
                break
            }
            bottomLeftX++
            bottomLeftY--
        }

        var bottomRightFlippable = false
        var bottomRightX = x + 1
        var bottomRightY = y + 1
        while (bottomRightX < BOARD_SIZE && bottomRightY < BOARD_SIZE && getTile(bottomRightX, bottomRightY).isWhite != currentTile.isWhite) {
            if (getTile(bottomRightX, bottomRightY).isEmpty()) {
                bottomRightFlippable = true
                break
            }
            bottomRightX++
            bottomRightY++
        }

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
