package xyz.teamgravity.friendchat.fragment

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.friendchat.databinding.FragmentChannelBinding

@AndroidEntryPoint
class ChannelFragment : BindingFragment<FragmentChannelBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChannelBinding::inflate
}