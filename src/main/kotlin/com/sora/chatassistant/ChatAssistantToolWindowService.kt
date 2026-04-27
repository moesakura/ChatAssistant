package com.sora.chatassistant

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

@Service(Service.Level.PROJECT)
class ChatAssistantToolWindowService(private val project: Project) {
    private var textArea: JBTextArea? = null
    private val pendingReferences = mutableListOf<String>()

    fun createComponent(): JComponent {
        val area = JBTextArea().apply {
            lineWrap = true
            wrapStyleWord = true
        }
        textArea = area

        pendingReferences.forEach { appendToArea(area, it) }
        pendingReferences.clear()
        moveCaretToEnd(area)

        return JPanel(BorderLayout()).apply {
            add(JBScrollPane(area), BorderLayout.CENTER)
        }
    }

    fun appendReference(reference: String) {
        val area = textArea
        if (area == null) {
            pendingReferences += reference
            return
        }

        appendToArea(area, reference)
        focusEditorAtEnd()
    }

    fun focusEditorAtEnd() {
        val area = textArea ?: return
        moveCaretToEnd(area)
        IdeFocusManager.getInstance(project).requestFocus(area, true)
    }

    private fun appendToArea(area: JBTextArea, reference: String) {
        area.text = ChatAssistantTextAppender.append(area.text, reference)
        moveCaretToEnd(area)
    }

    private fun moveCaretToEnd(area: JBTextArea) {
        area.caretPosition = area.document.length
    }
}
