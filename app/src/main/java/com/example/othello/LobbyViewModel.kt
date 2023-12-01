package com.example.othello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.garrit.android.multiplayer.Game
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.SupabaseService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LobbyViewModel : ViewModel() {

    val player = SupabaseService.player
    val users = SupabaseService.users
    val games = SupabaseService.games
    val serverState = SupabaseService.serverState.value

    sealed class Event {
        data class JoinGame(val game: Game) : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()


    fun joinGame(game: Game) {
        viewModelScope.launch {
            eventChannel.send(Event.JoinGame(game))
        }
    }

    fun sendGameInvitation(opponent: Player) {
        viewModelScope.launch {
            SupabaseService.invite(opponent)
        }
    }

    fun acceptGameInvitation(game: Game) {
        viewModelScope.launch {
            SupabaseService.acceptInvite(game)
        }
    }

    fun declineGameInvitation(game: Game) {
        viewModelScope.launch {
            SupabaseService.declineInvite(game)
        }
    }

    fun joinLobby(player: Player) {
        viewModelScope.launch {
            SupabaseService.joinLobby(player)
        }
    }

    fun leaveLobby() {
        viewModelScope.launch {
            SupabaseService.leaveLobby()
        }
    }

}
