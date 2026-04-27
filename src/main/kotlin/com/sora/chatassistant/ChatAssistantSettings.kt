package com.sora.chatassistant

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

enum class PathType { RELATIVE, ABSOLUTE }

enum class ReferenceStyle { CLI, AT }

@Service(Service.Level.APP)
@State(name = "ChatAssistantSettings", storages = [Storage("chatassistant.xml")])
class ChatAssistantSettings : PersistentStateComponent<ChatAssistantSettings.State> {
    data class State(
        var pathType: PathType = PathType.ABSOLUTE,
        var referenceStyle: ReferenceStyle = ReferenceStyle.CLI,
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(loaded: State) {
        XmlSerializerUtil.copyBean(loaded, state)
    }

    companion object {
        fun getInstance(): ChatAssistantSettings = service()
    }
}
