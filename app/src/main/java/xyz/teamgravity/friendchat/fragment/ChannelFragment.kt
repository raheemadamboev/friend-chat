package xyz.teamgravity.friendchat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import kotlinx.coroutines.flow.collect
import xyz.teamgravity.friendchat.R
import xyz.teamgravity.friendchat.arch.viewmodel.ChannelViewModel
import xyz.teamgravity.friendchat.databinding.FragmentChannelBinding
import xyz.teamgravity.friendchat.helper.extension.navigateSafely
import javax.inject.Inject

@AndroidEntryPoint
class ChannelFragment : BindingFragment<FragmentChannelBinding>() {

    @Inject
    lateinit var channelListViewModelFactory: ChannelListViewModelFactory

    private val channelViewModel by activityViewModels<ChannelViewModel>()

    private lateinit var channelListViewModel: ChannelListViewModel
    private lateinit var channelListHeaderViewModel: ChannelListHeaderViewModel

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChannelBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel()
        updateUI()
        button()
    }

    private fun viewmodel() {
        channelListViewModel = ViewModelProvider(this, channelListViewModelFactory).get(ChannelListViewModel::class.java)
        channelListHeaderViewModel = ViewModelProvider(this).get(ChannelListHeaderViewModel::class.java)
    }

    private fun updateUI() {
        binding.apply {
            // get chats to recycler view
            channelListViewModel.bindView(channelListView, viewLifecycleOwner)

            // update header
            channelListHeaderViewModel.bindView(channelListHeaderView, viewLifecycleOwner)

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                channelViewModel.createChannelEvent.collect { event ->
                    when (event) {
                        is ChannelViewModel.ChannelEvent.Error ->
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                        ChannelViewModel.ChannelEvent.Success ->
                            Toast.makeText(requireContext(), getString(R.string.channel_created), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun button() {
        onAvatar()
        onChannel()
        onHeader()
    }

    // avatar button -> disconnect
    private fun onAvatar() {
        binding.channelListHeaderView.setOnUserAvatarClickListener {
            channelViewModel.disconnect()
            findNavController().popBackStack()
        }
    }

    // channel click
    private fun onChannel() {
        binding.channelListView.setChannelItemClickListener { channel ->
            findNavController().navigateSafely(ChannelFragmentDirections.actionChannelFragmentToChatFragment(channel.cid))
        }
    }

    // header button
    private fun onHeader() {
        binding.channelListHeaderView.setOnActionButtonClickListener {
            findNavController().navigateSafely(ChannelFragmentDirections.actionChannelFragmentToChannelDialog())
        }
    }
}