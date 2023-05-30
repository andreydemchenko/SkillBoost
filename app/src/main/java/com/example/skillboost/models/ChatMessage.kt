package com.example.skillboost.models

data class ChatMessage(
    val id: String,
    val content: String,
    val sender: Sender
)

enum class Sender {
    Me,
    Bot
}