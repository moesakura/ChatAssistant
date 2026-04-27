package com.sora.chatassistant

import com.intellij.openapi.options.Configurable
import com.intellij.ui.EnumComboBoxModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import javax.swing.ComboBoxModel
import javax.swing.JComboBox
import javax.swing.JComponent

class ChatAssistantConfigurable : Configurable {
    private val pathTypeModel: ComboBoxModel<PathType> = EnumComboBoxModel(PathType::class.java)
    private val referenceStyleModel: ComboBoxModel<ReferenceStyle> = EnumComboBoxModel(ReferenceStyle::class.java)
    private lateinit var pathTypeCombo: JComboBox<PathType>
    private lateinit var referenceStyleCombo: JComboBox<ReferenceStyle>
    private val previewLabel = JBLabel()
    private var rootPanel: JComponent? = null

    override fun getDisplayName(): String = "ChatAssistant"

    override fun createComponent(): JComponent {
        val state = ChatAssistantSettings.getInstance().state
        pathTypeModel.selectedItem = state.pathType
        referenceStyleModel.selectedItem = state.referenceStyle

        val panel = panel {
            row("Path type:") {
                pathTypeCombo = comboBox(pathTypeModel, pathTypeRenderer())
                    .align(AlignX.FILL)
                    .component
                    .also { combo -> combo.addActionListener { updatePreview() } }
            }
            row("Reference style:") {
                referenceStyleCombo = comboBox(referenceStyleModel, referenceStyleRenderer())
                    .align(AlignX.FILL)
                    .component
                    .also { combo -> combo.addActionListener { updatePreview() } }
            }
            row("Preview:") {
                cell(previewLabel).align(AlignX.FILL)
            }
        }
        updatePreview()
        rootPanel = panel
        return panel
    }

    override fun isModified(): Boolean {
        val state = ChatAssistantSettings.getInstance().state
        return pathTypeModel.selectedItem != state.pathType ||
            referenceStyleModel.selectedItem != state.referenceStyle
    }

    override fun apply() {
        val state = ChatAssistantSettings.getInstance().state
        state.pathType = pathTypeModel.selectedItem as PathType
        state.referenceStyle = referenceStyleModel.selectedItem as ReferenceStyle
        updatePreview()
    }

    override fun reset() {
        val state = ChatAssistantSettings.getInstance().state
        pathTypeModel.selectedItem = state.pathType
        referenceStyleModel.selectedItem = state.referenceStyle
        updatePreview()
    }

    override fun disposeUIResources() {
        rootPanel = null
    }

    private fun updatePreview() {
        val pathType = pathTypeModel.selectedItem as? PathType ?: return
        val style = referenceStyleModel.selectedItem as? ReferenceStyle ?: return
        val samplePath = when (pathType) {
            PathType.RELATIVE -> "src/main/App.kt"
            PathType.ABSOLUTE -> "/Users/me/project/src/main/App.kt"
        }
        previewLabel.text = ChatReferenceFormatter.formatReference(samplePath, 12..18, style)
    }

    private fun pathTypeRenderer() = com.intellij.ui.SimpleListCellRenderer.create<PathType>("") {
        when (it) {
            PathType.RELATIVE -> "Relative to project root"
            PathType.ABSOLUTE -> "Absolute"
            else -> ""
        }
    }

    private fun referenceStyleRenderer() = com.intellij.ui.SimpleListCellRenderer.create<ReferenceStyle>("") {
        when (it) {
            ReferenceStyle.CLI -> "CLI (path:20-21)"
            ReferenceStyle.AT -> "At (@path#L20-21)"
            else -> ""
        }
    }
}
