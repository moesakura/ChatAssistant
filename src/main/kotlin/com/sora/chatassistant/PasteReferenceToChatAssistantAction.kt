package com.sora.chatassistant

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.ToolWindowManager

class PasteReferenceToChatAssistantAction : DumbAwareAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabled = e.project != null && editor != null && file != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val settings = ChatAssistantSettings.getInstance().state
        val displayPath = ChatReferenceFormatter.resolveDisplayPath(
            project = project,
            file = file,
            pathType = settings.pathType,
        )
        val selectionModel = editor.selectionModel
        val lineRange = if (selectionModel.hasSelection()) {
            ChatReferenceFormatter.computeSelectedLineRange(
                document = editor.document,
                selectionStart = selectionModel.selectionStart,
                selectionEnd = selectionModel.selectionEnd,
            )
        } else {
            null
        }
        val reference = ChatReferenceFormatter.formatReference(
            path = displayPath,
            lineRange = lineRange,
            style = settings.referenceStyle,
        )
        val toolWindowService = project.service<ChatAssistantToolWindowService>()

        toolWindowService.appendReference(reference)
        ToolWindowManager.getInstance(project)
            .getToolWindow(ChatAssistantConstants.TOOL_WINDOW_ID)
            ?.show { toolWindowService.focusEditorAtEnd() }
    }
}
