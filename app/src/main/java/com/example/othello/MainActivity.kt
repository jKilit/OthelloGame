package com.example.othello

import LobbyScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.othello.ui.theme.OthelloTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OthelloTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showBackButton by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var topBarTitle by rememberSaveable {
                        mutableStateOf("Othello Game")
                    }

                    val navController = rememberNavController()

                    var isDarkMode by remember { mutableStateOf(false) }

                    Scaffold(
                        topBar = {
                            if (showBackButton || topBarTitle.isNotEmpty()) {
                                TopAppBar(
                                    title = {
                                        Text(
                                            text = topBarTitle,
                                            color = if (isDarkMode) Color.White else Color.Black
                                        )
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color.Transparent // Ensuring transparency
                                    ),
                                    navigationIcon = {
                                        if (showBackButton) {
                                            IconButton(onClick = { navController.navigateUp() }) {
                                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                            }
                                        }
                                    }
                                )
                            }
                        },
                        containerColor = Color.Transparent,
                        content = { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = Screen.Start.route
                                ) {
                                    composable(route = Screen.Start.route) {
                                        topBarTitle = ""
                                        showBackButton = false
                                        StartScreen(navController = navController, viewModel = LobbyViewModel(), isDarkMode)
                                    }
                                    composable(route = Screen.Game.route) {
                                        topBarTitle = "Exit Game"
                                        showBackButton = true
                                        GameScreen(navController = navController, viewModel = viewModel(), isDarkMode = isDarkMode)
                                    }
                                    composable(route = Screen.Settings.route) {
                                        topBarTitle = "Back to Home"
                                        showBackButton = true
                                        SettingsScreen(navController = navController,isDarkMode ) {
                                            isDarkMode = it
                                        }
                                    }
                                    composable(Screen.Rules.route) {
                                        topBarTitle = "Back to Home"
                                        showBackButton = true
                                        RulesScreen(isDarkMode)
                                    }
                                    composable(route = Screen.Lobby.route) {
                                        topBarTitle = "Back to Home"
                                        showBackButton = true
                                        LobbyScreen(navController = navController, viewModel = LobbyViewModel(), isDarkMode)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

