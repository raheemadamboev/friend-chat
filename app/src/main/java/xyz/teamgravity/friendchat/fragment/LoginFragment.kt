package xyz.teamgravity.friendchat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import xyz.teamgravity.friendchat.R
import xyz.teamgravity.friendchat.arch.viewmodel.LoginViewModel
import xyz.teamgravity.friendchat.databinding.FragmentLoginBinding
import xyz.teamgravity.friendchat.helper.extension.*

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>() {

    private val viewmodel by viewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button()
        observe()
    }

    private fun button() {
        onConfirm()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.loginEvent.collect { event ->
                when (event) {
                    is LoginViewModel.LoginEvent.Error -> {
                        enable(true)
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }

                    LoginViewModel.LoginEvent.Success -> {
                        enable(true)
                        findNavController().navigateSafely(LoginFragmentDirections.actionLoginFragmentToChannelFragment())
                    }
                }
            }
        }
    }

    private fun enable(enabled: Boolean) {
        binding.apply {
            if (enabled) {
                confirmB.enable()
                progressBar.gone()
            } else {
                confirmB.disable()
                progressBar.visible()
            }
        }
    }

    private fun validateInputs() {
        binding.apply {
            usernameField.clearErrorFocus()

            val username = usernameField.text()

            if (username.length < 4) {
                usernameField.error(getString(R.string.error_username_too_short))
                return
            }

            enable(false)
            viewmodel.connectUser(username)
        }
    }

    private fun onConfirm() {
        binding.confirmB.setOnClickListener {
            validateInputs()
        }
    }
}