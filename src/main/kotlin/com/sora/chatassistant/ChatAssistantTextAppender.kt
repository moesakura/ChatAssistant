package com.sora.chatassistant

object ChatAssistantTextAppender {
    fun append(existingText: String, reference: String): String = when {
        existingText.isEmpty() -> reference
        existingText.endsWith("\n") -> existingText + reference
        else -> "$existingText\n$reference"
    }
}
