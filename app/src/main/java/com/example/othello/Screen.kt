package com.example.othello

sealed class Screen(val route: String) {
    object Start : Screen(route = "start")
    object Game : Screen(route = "game")

}