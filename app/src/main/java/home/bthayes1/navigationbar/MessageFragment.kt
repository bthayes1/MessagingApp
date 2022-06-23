package home.bthayes1.navigationbar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import home.bthayes1.navigationbar.databinding.FragmentMessageBinding
import home.bthayes1.navigationbar.databinding.FragmentSignUpBinding
import home.bthayes1.navigationbar.models.LoginActivityViewModel


class MessageFragment : Fragment() {

    private var binding : FragmentMessageBinding? = null
    private val sharedViewModel: LoginActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentMessageBinding.inflate(inflater, container, false)
        sharedViewModel.getLoggedStatus().observe(viewLifecycleOwner) {loggedIn ->
            Log.i(TAG, "LoggedStatus: $loggedIn")
            if (!loggedIn){
                goToSignIn()
            }
        }
        binding = fragmentBinding
        return fragmentBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewModel = sharedViewModel
            btnSignOut.setOnClickListener {
                Log.i(TAG, "btnSignOut.setOnClickListener" )
                sharedViewModel.signOut() }
        }
    }

    private fun goToSignIn() {
        findNavController().navigate(R.id.action_messageFragment_to_signInFragment)
    }

    companion object {
       private const val TAG = "MessageFragment"
    }
}