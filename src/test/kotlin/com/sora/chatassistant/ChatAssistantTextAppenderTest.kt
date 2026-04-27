package com.sora.chatassistant

import kotlin.test.Test
import kotlin.test.assertEquals

class ChatAssistantTextAppenderTest {
    @Test
    fun `append writes directly into an empty panel`() {
        assertEquals(
            expected = "src/main/App.kt:12",
            actual = ChatAssistantTextAppender.append("", "src/main/App.kt:12"),
        )
    }

    @Test
    fun `append adds a newline after existing text`() {
        assertEquals(
            expected = "first\nsecond",
            actual = ChatAssistantTextAppender.append("first", "second"),
        )
    }

    @Test
    fun `append does not add an extra blank line when text already ends with newline`() {
        assertEquals(
            expected = "first\nsecond",
            actual = ChatAssistantTextAppender.append("first\n", "second"),
        )
    }
}
