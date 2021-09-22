package xyz.teamgravity.friendchat.arch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    private val _createChannelEvent = MutableSharedFlow<ChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()

    fun disconnect() = client.disconnect()

    fun user(): User? = client.getCurrentUser()

    fun createChannel(name: String) = viewModelScope.launch {
        if (name.isEmpty()) {
            _createChannelEvent.emit(ChannelEvent.Error("Please fill channel name"))
            return@launch
        }

        val result = client.createChannel(
            channelType = "messaging",
            channelId = UUID.randomUUID().toString(),
            extraData = mapOf("name" to name)
        ).await()

        if (result.isError) {
            _createChannelEvent.emit(ChannelEvent.Error(result.error().message ?: "Error"))
            return@launch
        }

        _createChannelEvent.emit(ChannelEvent.Success)
    }

    sealed class ChannelEvent {
        data class Error(val message: String) : ChannelEvent()
        object Success : ChannelEvent()
    }
}