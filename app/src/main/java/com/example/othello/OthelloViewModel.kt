package com.example.othello

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

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

class OthelloViewModel : ViewModel() {

    companion object {
        const val BOARD_SIZE = 5
    }

    private val gameBoard: List<List<Tile>> = List(BOARD_SIZE) { x -> //2d lista för logik
        List(BOARD_SIZE) { y ->
            println("$x, $y")
            Tile(x, y, isBlack = false, isWhite = false)
        }
    }

    // MutableStateList is used to observe changes to the list in Compose
     val boardState = mutableStateListOf<Tile>().apply {//skapar en 1d lista av 2d gameboard och lägger den i boardstate
        addAll(gameBoard.flatten())
    }

    internal var isBlackTurn = true
        private set

    init { //initziering block, koden körs när othello classen skapas
        // Initialize the starting position of Othello
        makeBlack(3, 3)
        makeWhite(3, 4)
        makeWhite(4, 3)
        makeBlack(4, 4)
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

    fun makeBlack(x: Int, y: Int) {
        var current= getTile(x,y)
        current.isBlack = true
        current.isWhite = false
    }

    fun makeWhite(x: Int, y: Int) {
        var current= getTile(x,y)
        current.isBlack = false
        current.isWhite = true
    }

    fun checkIsGameOver(): Boolean {
        // Check if there are any valid moves for the current player
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                val tile = getTile(i, j)
                if (isValidMove(tile)) {
                    return false // There is at least one valid move, game is not over
                }
            }
        }
        return true // No valid moves left, game is over
    }

    // MutableState to track if the game is over
    var isGameOver by mutableStateOf(false)
        private set

    // MutableState to store the winner
    var winner by mutableStateOf<String?>(null)
        private set


    // Function to handle a move
    fun makeMove(x: Int, y: Int, navController: NavController) {
        val selectedTile = getTile(x, y)

        if (isValidMove(selectedTile)) {
            if (isBlackTurn) {
                makeBlack(x, y)
            } else {
                makeWhite(x, y)
            }
            updateBoardState()
            flipTiles(x, y)
            isBlackTurn = !isBlackTurn

            if (checkIsGameOver()) {
                val (blackScore, whiteScore) = getScores()
                val winner = when {
                    blackScore > whiteScore -> "Black"
                    whiteScore > blackScore -> "White"
                    else -> "It's a tie"
                }

                navController.navigate("${Screen.GameOver.route}/$winner/$blackScore/$whiteScore")
            }
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

                val newX = tile.x + i
                val newY = tile.y + j

                if (newX in 0 until BOARD_SIZE && newY in 0 until BOARD_SIZE) {
                    val adjacentTile = getTile(newX, newY)

                    if (isValidDirection(tile, adjacentTile, i, j)) {
                        return true
                    }
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
        if (isBlack) {
            makeBlack(x,y)
        } else {
            makeWhite(x,y)
        }
    }

    // Flip tiles based on the placed piece
    private fun flipTiles(x: Int, y: Int) {
        flipHorizontal(x, y)
        flipVertical(x, y)
        flipDiagonal(x, y)
    }

    private fun flipHorizontal(x: Int, y: Int) {
        if (hasFlippableTilesHorizontal(x, y)) {
            val currentTile = getTile(x, y)

            // Flip tiles to the left
            if (hasFlippableTilesHorizontalDirection(x, y, -1)) {
                flipDirection(x, y, -1, 0)
            }

            // Flip tiles to the right
            if (hasFlippableTilesHorizontalDirection(x, y, 1)) {
                flipDirection(x, y, 1, 0)
            }
        }
    }

    private fun flipVertical(x: Int, y: Int) {
        if (hasFlippableTilesVertical(x, y)) {
            val currentTile = getTile(x, y)

            // Flip tiles up
            if (hasFlippableTilesVerticalDirection(x, y, -1)) {
                flipDirection(x, y, 0, -1)
            }

            // Flip tiles down
            if (hasFlippableTilesVerticalDirection(x, y, 1)) {
                flipDirection(x, y, 0, 1)
            }
        }
    }

    private fun hasFlippableTilesHorizontalDirection(startX: Int, startY: Int, deltaX: Int): Boolean { //https://chat.openai.com/share/23507c7b-ba03-40d8-841c-504827580221
        val currentTile = getTile(startX, startY)

        var x = startX + deltaX

        while (x in 0 until BOARD_SIZE) {
            val currentDirectionTile = getTile(x, startY)

            if (currentDirectionTile.isEmpty()) {
                return false // Empty tile, invalid move
            } else if ((isBlackTurn && currentDirectionTile.isBlack) || (!isBlackTurn && currentDirectionTile.isWhite)) {
                return true // Found a valid sequence to flip
            }

            x += deltaX
        }

        return false
    }

    private fun hasFlippableTilesVerticalDirection(startX: Int, startY: Int, deltaY: Int): Boolean { //https://chat.openai.com/share/23507c7b-ba03-40d8-841c-504827580221
        val currentTile = getTile(startX, startY)

        var y = startY + deltaY

        while (y in 0 until BOARD_SIZE) {
            val currentDirectionTile = getTile(startX, y)

            if (currentDirectionTile.isEmpty()) {
                return false // Empty tile, invalid move
            } else if ((isBlackTurn && currentDirectionTile.isBlack) || (!isBlackTurn && currentDirectionTile.isWhite)) {
                return true // Found a valid sequence to flip
            }

            y += deltaY
        }

        return false
    }

    //logic based on the chat from the other flip functions
    private fun flipDiagonal(x: Int, y: Int) {
        if (hasFlippableTilesDiagonal(x, y)) {
            val currentTile = getTile(x, y)

            // Check top-left direction
            if (hasFlippableTilesDiagonalDirection(x, y, -1, -1)) {
                flipDirection(x, y, -1, -1)
            }

            // Check top-right direction
            if (hasFlippableTilesDiagonalDirection(x, y, 1, -1)) {
                flipDirection(x, y, 1, -1)
            }

            // Check bottom-left direction
            if (hasFlippableTilesDiagonalDirection(x, y, -1, 1)) {
                flipDirection(x, y, -1, 1)
            }

            // Check bottom-right direction
            if (hasFlippableTilesDiagonalDirection(x, y, 1, 1)) {
                flipDirection(x, y, 1, 1)
            }
        }
    }

    private fun hasFlippableTilesDiagonalDirection(startX: Int, startY: Int, deltaX: Int, deltaY: Int): Boolean {
        val currentTile = getTile(startX, startY)

        var x = startX + deltaX
        var y = startY + deltaY

        while (x in 0 until BOARD_SIZE && y in 0 until BOARD_SIZE) {
            val currentDirectionTile = getTile(x, y)

            if (currentDirectionTile.isEmpty()) {
                return false // Empty tile, invalid move
            } else if ((isBlackTurn && currentDirectionTile.isBlack) || (!isBlackTurn && currentDirectionTile.isWhite)) {
                return true // Found a valid sequence to flip
            }

            x += deltaX
            y += deltaY
        }

        return false
    }


    private fun flipDirection(startX: Int, startY: Int, deltaX: Int, deltaY: Int) { //https://chat.openai.com/share/23507c7b-ba03-40d8-841c-504827580221
        var x = startX + deltaX
        var y = startY + deltaY
        while (x in 0 until BOARD_SIZE && y in 0 until BOARD_SIZE) {
            val currentTile = getTile(x, y)

            if (currentTile.isEmpty() || (isBlackTurn && currentTile.isBlack) || (!isBlackTurn && currentTile.isWhite)) {
                break // Empty tile or tile of the same color, stop flipping
            }

            flip(x, y)
            x += deltaX
            y += deltaY
        }
    }

    private fun hasFlippableTilesHorizontal(x: Int, y: Int): Boolean {
        val currentTile = getTile(x, y)

        // Check to the left
        var leftFlippable = false
        var leftX = x - 1
        while (leftX >= 0) {
            val leftTile = getTile(leftX, y)
            if (leftTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (leftTile.isEmpty()) {
                leftFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                leftFlippable = true
            }
            leftX--
        }
        while (leftX >= 0) {
            val leftTile = getTile(leftX, y)
            if (leftTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (leftTile.isEmpty()) {
                leftFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                leftFlippable = true
            }
            leftX--
        }

        // Check to the right
        var rightFlippable = false
        var rightX = x + 1
        while (rightX < BOARD_SIZE) {
            val rightTile = getTile(rightX, y)
            if (rightTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (rightTile.isEmpty()) {
                rightFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                rightFlippable = true
            }
            rightX++
        }
        while (rightX < BOARD_SIZE) {
            val rightTile = getTile(rightX, y)
            if (rightTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (rightTile.isEmpty()) {
                rightFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                rightFlippable = true
            }
            rightX++
        }

        return leftFlippable || rightFlippable
    }



    private fun hasFlippableTilesVertical(x: Int, y: Int): Boolean {
        val currentTile = getTile(x, y)

        var upFlippable = false
        var upY = y - 1
        while (upY >= 0) {
            val upTile = getTile(x, upY)
            if (upTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (upTile.isEmpty()) {
                upFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                upFlippable = true
            }
            upY--
        }
        while (upY >= 0) {
            val upTile = getTile(x, upY)
            if (upTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (upTile.isEmpty()) {
                upFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                upFlippable = true
            }
            upY--
        }

        var downFlippable = false
        var downY = y + 1
        while (downY < BOARD_SIZE) {
            val downTile = getTile(x, downY)
            if (downTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (downTile.isEmpty()) {
                downFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                downFlippable = true
            }
            downY++
        }
        while (downY < BOARD_SIZE) {
            val downTile = getTile(x, downY)
            if (downTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (downTile.isEmpty()) {
                downFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                downFlippable = true
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
        while (topLeftX >= 0 && topLeftY >= 0) {
            val topLeftTile = getTile(topLeftX, topLeftY)
            if (topLeftTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (topLeftTile.isEmpty()) {
                topLeftFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                topLeftFlippable = true
            }
            topLeftX--
            topLeftY--
        }
        while (topLeftX >= 0 && topLeftY >= 0) {
            val topLeftTile = getTile(topLeftX, topLeftY)
            if (topLeftTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (topLeftTile.isEmpty()) {
                topLeftFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                topLeftFlippable = true
            }
            topLeftX--
            topLeftY--
        }

        var topRightFlippable = false
        var topRightX = x - 1
        var topRightY = y + 1
        while (topRightX >= 0 && topRightY < BOARD_SIZE) {
            val topRightTile = getTile(topRightX, topRightY)
            if (topRightTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (topRightTile.isEmpty()) {
                topRightFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                topRightFlippable = true
            }
            topRightX--
            topRightY++
        }
        while (topRightX >= 0 && topRightY < BOARD_SIZE) {
            val topRightTile = getTile(topRightX, topRightY)
            if (topRightTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (topRightTile.isEmpty()) {
                topRightFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                topRightFlippable = true
            }
            topRightX--
            topRightY++
        }

        var bottomLeftFlippable = false
        var bottomLeftX = x + 1
        var bottomLeftY = y - 1
        while (bottomLeftX < BOARD_SIZE && bottomLeftY >= 0) {
            val bottomLeftTile = getTile(bottomLeftX, bottomLeftY)
            if (bottomLeftTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (bottomLeftTile.isEmpty()) {
                bottomLeftFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                bottomLeftFlippable = true
            }
            bottomLeftX++
            bottomLeftY--
        }
        while (bottomLeftX < BOARD_SIZE && bottomLeftY >= 0) {
            val bottomLeftTile = getTile(bottomLeftX, bottomLeftY)
            if (bottomLeftTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (bottomLeftTile.isEmpty()) {
                bottomLeftFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                bottomLeftFlippable = true
            }
            bottomLeftX++
            bottomLeftY--
        }

        var bottomRightFlippable = false
        var bottomRightX = x + 1
        var bottomRightY = y + 1
        while (bottomRightX < BOARD_SIZE && bottomRightY < BOARD_SIZE) {
            val bottomRightTile = getTile(bottomRightX, bottomRightY)
            if (bottomRightTile.isWhite == currentTile.isWhite) {
                break // Stop if we find a tile of the same color
            } else if (bottomRightTile.isEmpty()) {
                bottomRightFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                bottomRightFlippable = true
            }
            bottomRightX++
            bottomRightY++
        }
        while (bottomRightX < BOARD_SIZE && bottomRightY < BOARD_SIZE) {
            val bottomRightTile = getTile(bottomRightX, bottomRightY)
            if (bottomRightTile.isBlack == currentTile.isBlack) {
                break // Stop if we find a tile of the same color
            } else if (bottomRightTile.isEmpty()) {
                bottomRightFlippable = false
                break // We found an empty tile, but it's not flippable in this direction
            } else {
                bottomRightFlippable = true
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
        val blackScore = boardState.count { it.isBlack }
        val whiteScore = boardState.count { it.isWhite }

        return Pair(blackScore, whiteScore)
    }

}

