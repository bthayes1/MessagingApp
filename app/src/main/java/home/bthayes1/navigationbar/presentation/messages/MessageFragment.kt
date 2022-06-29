package home.bthayes1.navigationbar.presentation.messages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import home.bthayes1.navigationbar.R
import home.bthayes1.navigationbar.databinding.FragmentMessageBinding
import home.bthayes1.navigationbar.presentation.login.LoginActivityViewModel

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private var binding : FragmentMessageBinding? = null
    private val messageViewModel: MessagesViewModel by activityViewModels()
    private val authViewModel: LoginActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentMessageBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        Log.i(TAG, "MessageFragment: ${messageViewModel.getLoggedIn().value}")
        return fragmentBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewModel = messageViewModel
            viewModel2 = authViewModel
            btnSignOut.setOnClickListener {
                authViewModel.signout()
                goToSignIn()
            }
        }
    }

    private fun goToSignIn() {
        findNavController().navigate(R.id.action_messageFragment_to_signInFragment)
    }

    companion object {
       private const val TAG = "MessageFragment"
    }
}