package com.example.skillboost.viewmodels

import androidx.lifecycle.ViewModel
import com.example.skillboost.models.ChatMessage
import com.example.skillboost.models.Sender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class MentorViewModel: ViewModel() {
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(ChatMessage(
        id = UUID.randomUUID().toString(),
        content = "✧ Which programming language should I learn to become a mobile game developer?\n✧ What are the daily tasks of a front-end developer?",
        sender = Sender.Bot
    ),
        ChatMessage(
            id = UUID.randomUUID().toString(),
            content = "I want to learn how to play the dota 2",
            sender = Sender.Me
        ),
        ChatMessage(
            id = UUID.randomUUID().toString(),
            content = "Please wait...",
            sender = Sender.Bot
        ),
        ChatMessage(
            id = UUID.randomUUID().toString(),
            content = "To learn how to play Dota 2, here are some steps you can follow: \n" +
                    "\n" +
                    "1. Familiarize yourself with the game mechanics: Dota 2 is a complex game with a lot of different mechanics to learn. Take some time to read through the game's tutorial and practice mode to get a feel for how everything works. ",
            sender = Sender.Bot
        )))
    val chatMessages: StateFlow<List<ChatMessage>> get() = _chatMessages

    private val _showHistory = MutableStateFlow(false)
    val showHistory: StateFlow<Boolean> get() = _showHistory

    fun addChatMessage(chatMessage: ChatMessage) {
//        val updatedList = _chatMessages.value + chatMessage
//        _chatMessages.value = updatedList
        _chatMessages.value += chatMessage
    }

    fun openHistoryScreen() {
        _showHistory.value = true
    }

    fun closeHistoryScreen() {
        _showHistory.value = false
    }
}