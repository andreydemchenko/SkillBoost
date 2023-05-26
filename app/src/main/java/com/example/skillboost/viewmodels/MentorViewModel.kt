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
            content = "✧ Which programming language should I learn to become a mobile game developer?\n✧ What are the daily tasks of a front-end developer?",
            sender = Sender.Me
        ),
        ChatMessage(
            id = UUID.randomUUID().toString(),
            content = "✧ Which programming language should I learn to become a mobile game developer?\n✧ What are the daily tasks of a front-end developer?",
            sender = Sender.Bot
        ),
        ChatMessage(
            id = UUID.randomUUID().toString(),
            content = "✧ Which programming language should I learn to become a mobile game developer?\n✧ What are the daily tasks of a front-end developer?",
            sender = Sender.Me
        ),
        ChatMessage(
            id = UUID.randomUUID().toString(),
            content = "✧ Which programming language should I learn to become a mobile game developer?\n✧ What are the daily tasks of a front-end developer?",
            sender = Sender.Bot
        ),
        ChatMessage(
            id = UUID.randomUUID().toString(),
            content = "✧ Which programming language should I learn to become a mobile game developer?\n✧ What are the daily tasks of a front-end developer?",
            sender = Sender.Me
        )))
    val chatMessages: StateFlow<List<ChatMessage>> get() = _chatMessages

    fun addChatMessage(chatMessage: ChatMessage) {
//        val updatedList = _chatMessages.value + chatMessage
//        _chatMessages.value = updatedList
        _chatMessages.value += chatMessage
    }
}