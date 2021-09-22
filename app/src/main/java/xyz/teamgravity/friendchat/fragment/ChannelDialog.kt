package xyz.teamgravity.friendchat.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.friendchat.R
import xyz.teamgravity.friendchat.arch.viewmodel.ChannelViewModel
import xyz.teamgravity.friendchat.databinding.DialogChannelBinding
import xyz.teamgravity.friendchat.helper.extension.text

@AndroidEntryPoint
class ChannelDialog : DialogFragment() {

    private var _binding: DialogChannelBinding? = null
    private val binding get() = _binding!!

    private val viewmodel by activityViewModels<ChannelViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChannelBinding.inflate(layoutInflater)
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.choose_channel_name)
            .setView(binding.root)
            .setPositiveButton(R.string.create) { _, _ ->
                viewmodel.createChannel(binding.channelNameField.text())
            }.setNegativeButton(R.string.cancel, null)
            .create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}