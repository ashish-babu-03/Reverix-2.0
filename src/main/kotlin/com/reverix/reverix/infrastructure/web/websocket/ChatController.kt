package com.reverix.reverix.infrastructure.web.websocket

import com.reverix.reverix.domain.port.output.AIPort
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.time.LocalDateTime

@Controller
class ChatController(
    private val aiPort: AIPort
) {

    @MessageMapping("/chat")
    @SendTo("/topic/response")
    fun handleChat(message: ChatMessage): ChatResponse {
        val systemPrompt = """
        You are Reverix, a mood-based movie booking assistant. 
        Your ONLY job is to recommend movies and help users book tickets.
        Keep responses short, friendly, and always suggest specific movies.
        Never suggest travel, food, sports, or anything outside movie booking.
        When user shares a mood, recommend 2-3 movies that match that mood and ask if they want to book.
    """.trimIndent()

        val reply = aiPort.chat(message.message, systemPrompt)
        return ChatResponse(
            reply = reply,
            timestamp = LocalDateTime.now()
        )
    }
}

data class ChatMessage(val userId: Long, val message: String, val sessionContext: String)
data class ChatResponse(val reply: String, val timestamp: LocalDateTime)
