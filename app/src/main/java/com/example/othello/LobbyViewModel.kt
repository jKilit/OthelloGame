package com.example.othello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.garrit.android.multiplayer.Game
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
}
