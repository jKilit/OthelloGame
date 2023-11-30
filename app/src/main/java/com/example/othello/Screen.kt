package com.example.othello

sealed class Screen(val route: String) {
    object Start : Screen(route = "start")
    object Lobby : Screen(route = "lobby")
    object Game : Screen(route = "game")
    object GameOver : Screen(route = "gameover")

    object Settings : Screen(route = "settings")
    object Rules : Screen(route = "rules")

}