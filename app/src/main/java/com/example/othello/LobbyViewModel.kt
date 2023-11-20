package com.example.othello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.garrit.android.multiplayer.Game
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.SupabaseService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LobbyViewModel : ViewModel() {


    //BARA BÖRJAN, en mall!
    sealed class Event {
        data class StartGame(val opponent: String, val difficulty: String) : Event()
        data class JoinGame(val game: Game) : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()



    fun joinGame(game: Game) {
        viewModelScope.launch {
            eventChannel.send(Event.JoinGame(game))
        }
    }
    fun joinLobby(playerName: String) {
        viewModelScope.launch {
            if(playerName.isNotBlank()) {
                val player = Player(name = playerName)
                SupabaseService.joinLobby(player)
            }
        }
    }
    fun leaveLobby(){
        viewModelScope.launch {
            SupabaseService.leaveLobby()
        }
    }

}
