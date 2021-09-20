package xyz.teamgravity.friendchat.arch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    // connect user with username
    fun connectUser(username: String) {
        viewModelScope.launch {
            val result = client.connectGuestUser(
                userId = username,
                username = username
            ).await()

            if (result.isError) {
                _loginEvent.emit(LoginEvent.Error(result.error().message ?: "Unknown error"))
                return@launch
            }

            _loginEvent.emit(LoginEvent.Success)
        }
    }

    sealed class LoginEvent {
        data class Error(val message: String) : LoginEvent()
        object Success : LoginEvent()
    }
}