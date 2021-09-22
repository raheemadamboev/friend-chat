package xyz.teamgravity.friendchat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory
import xyz.teamgravity.friendchat.databinding.FragmentChatBinding

class ChatFragment : BindingFragment<FragmentChatBinding>() {

    private val args by navArgs<ChatFragmentArgs>()

    private lateinit var messageListHeaderViewModel: MessageListHeaderViewModel
    private lateinit var messageListViewModel: MessageListViewModel
    private lateinit var messageInputViewModel: MessageInputViewModel

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChatBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel()
        updateUI()
        button()
    }

    private fun viewmodel() {
        val factory = MessageListViewModelFactory(args.channelId)
        messageListHeaderViewModel = ViewModelProvider(this, factory).get(MessageListHeaderViewModel::class.java)
        messageListViewModel = ViewModelProvider(this, factory).get(MessageListViewModel::class.java)
        messageInputViewModel = ViewModelProvider(this, factory).get(MessageInputViewModel::class.java)
    }

    private fun updateUI() {
        binding.apply {
            messageListHeaderViewModel.bindView(messageListHeaderView, viewLifecycleOwner)
            messageListViewModel.bindView(messageListView, viewLifecycleOwner)
            messageInputViewModel.bindView(messageInputView, viewLifecycleOwner)

            // change thread
            messageListViewModel.mode.observe(viewLifecycleOwner) { mode ->
                when (mode) {
                    is MessageListViewModel.Mode.Thread -> {
                        messageListHeaderViewModel.setActiveThread(mode.parentMessage)
                        messageInputViewModel.setActiveThread(mode.parentMessage)
                    }
                    MessageListViewModel.Mode.Normal -> {
                        messageListHeaderViewModel.resetThread()
                        messageInputViewModel.resetThread()
                    }
                }
            }

            // edit message connected
            messageListView.setMessageEditHandler(messageInputViewModel::postMessageToEdit)

            // state observe
            messageListViewModel.state.observe(viewLifecycleOwner) { state ->
                if (state is MessageListViewModel.State.NavigateUp) {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun button() {
        onBack()
    }

    private fun onBack() {
        // header view back button
        binding.messageListHeaderView.setBackButtonClickListener(onBackHandler)
        // back button of phone
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackHandler()
        }
    }

    private val onBackHandler = {
        // send back pressed state event
        messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
    }
}