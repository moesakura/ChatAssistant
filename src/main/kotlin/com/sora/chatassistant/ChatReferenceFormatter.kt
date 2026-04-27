package com.sora.chatassistant

import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import kotlin.math.max
import kotlin.math.min

object ChatReferenceFormatter {
    fun resolveDisplayPath(
        project: Project?,
        file: VirtualFile,
        pathType: PathType = PathType.ABSOLUTE,
    ): String {
        val absolutePath = FileUtil.toSystemIndependentName(file.path)
            .ifBlank { file.presentableUrl }

        if (pathType == PathType.ABSOLUTE) {
            return absolutePath
        }

        val projectBasePath = project?.basePath?.let(FileUtil::toSystemIndependentName)
            ?: return absolutePath

        val relativePath = FileUtil.getRelativePath(projectBasePath, absolutePath, '/')
        return if (!relativePath.isNullOrBlank() && !relativePath.startsWith("../")) {
            relativePath
        } else {
            absolutePath
        }
    }

    fun computeSelectedLineRange(
        document: Document,
        selectionStart: Int,
        selectionEnd: Int,
    ): IntRange? {
        if (selectionStart == selectionEnd || document.textLength == 0) {
            return null
        }

        val normalizedStart = min(selectionStart, selectionEnd)
        val normalizedEnd = max(selectionStart, selectionEnd)
        val startLine = document.getLineNumber(normalizedStart) + 1
        val inclusiveEndOffset = min(normalizedEnd - 1, document.textLength - 1)
        val endLine = document.getLineNumber(inclusiveEndOffset) + 1

        return startLine..endLine
    }

    fun formatReference(
        path: String,
        lineRange: IntRange?,
        style: ReferenceStyle = ReferenceStyle.CLI,
    ): String = when (style) {
        ReferenceStyle.CLI -> formatCli(path, lineRange)
        ReferenceStyle.AT -> formatAt(path, lineRange)
    }

    private fun formatCli(path: String, lineRange: IntRange?): String {
        if (lineRange == null) {
            return path
        }
        return if (lineRange.first == lineRange.last) {
            "$path:${lineRange.first}"
        } else {
            "$path:${lineRange.first}-${lineRange.last}"
        }
    }

    private fun formatAt(path: String, lineRange: IntRange?): String {
        val prefixed = "@$path"
        if (lineRange == null) {
            return prefixed
        }
        return if (lineRange.first == lineRange.last) {
            "$prefixed#L${lineRange.first}"
        } else {
            "$prefixed#L${lineRange.first}-${lineRange.last}"
        }
    }
}
