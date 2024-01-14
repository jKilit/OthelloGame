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

}