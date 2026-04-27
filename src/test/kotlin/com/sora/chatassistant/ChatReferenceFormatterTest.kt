package com.sora.chatassistant

import kotlin.test.Test
import kotlin.test.assertEquals

class ChatReferenceFormatterTest {
    @Test
    fun `cli style uses path only without line range`() {
        assertEquals(
            expected = "src/main/App.kt",
            actual = ChatReferenceFormatter.formatReference(
                path = "src/main/App.kt",
                lineRange = null,
                style = ReferenceStyle.CLI,
            ),
        )
    }

    @Test
    fun `cli style formats a single line`() {
        assertEquals(
            expected = "src/main/App.kt:12",
            actual = ChatReferenceFormatter.formatReference(
                path = "src/main/App.kt",
                lineRange = 12..12,
                style = ReferenceStyle.CLI,
            ),
        )
    }

    @Test
    fun `cli style formats a line range`() {
        assertEquals(
            expected = "/Users/me/project/src/main/App.kt:12-18",
            actual = ChatReferenceFormatter.formatReference(
                path = "/Users/me/project/src/main/App.kt",
                lineRange = 12..18,
                style = ReferenceStyle.CLI,
            ),
        )
    }

    @Test
    fun `at style uses path only without line range`() {
        assertEquals(
            expected = "@src/main/App.kt",
            actual = ChatReferenceFormatter.formatReference(
                path = "src/main/App.kt",
                lineRange = null,
                style = ReferenceStyle.AT,
            ),
        )
    }

    @Test
    fun `at style formats a single line`() {
        assertEquals(
            expected = "@src/main/App.kt#L12",
            actual = ChatReferenceFormatter.formatReference(
                path = "src/main/App.kt",
                lineRange = 12..12,
                style = ReferenceStyle.AT,
            ),
        )
    }

    @Test
    fun `at style formats a line range`() {
        assertEquals(
            expected = "@/Users/me/project/src/main/App.kt#L12-18",
            actual = ChatReferenceFormatter.formatReference(
                path = "/Users/me/project/src/main/App.kt",
                lineRange = 12..18,
                style = ReferenceStyle.AT,
            ),
        )
    }

    @Test
    fun `default style is cli`() {
        assertEquals(
            expected = "src/main/App.kt:12-18",
            actual = ChatReferenceFormatter.formatReference(
                path = "src/main/App.kt",
                lineRange = 12..18,
            ),
        )
    }
}
