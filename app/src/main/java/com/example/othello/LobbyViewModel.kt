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


    //BARA BÖRJAN, en mall! h
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
    fun leaveLobby(){
        viewModelScope.launch {
            SupabaseService.leaveLobby()
        }
    }

}
