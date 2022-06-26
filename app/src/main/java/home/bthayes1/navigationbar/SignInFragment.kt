package home.bthayes1.navigationbar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import home.bthayes1.navigationbar.databinding.FragmentSignInBinding
import home.bthayes1.navigationbar.models.LoginActivityViewModel
import androidx.navigation.fragment.findNavController


class SignInFragment : Fragment() {

    companion object{
        private const val TAG = "SignInFragment"
    }
    private var binding : FragmentSignInBinding? = null
    private val sharedViewModel: LoginActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSignInBinding.inflate(inflater, container, false)
        sharedViewModel.getLoggedStatus().observe(viewLifecycleOwner){loggedIn ->
            if (loggedIn){
                navigateToMessages()
            }
        }
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewModel = sharedViewModel
            tvSignUp.setOnClickListener { goToSignUp() }
            btnSignin.setOnClickListener {
                sharedViewModel.checkSignInEntries()
            }
        }
    }

    private fun navigateToMessages() {
        Log.i(TAG, "navigateToMessages: Starting")
        findNavController().navigate(R.id.action_signInFragment_to_messageFragment)
    }

    private fun goToSignUp(){
        Log.i(TAG, "goToSignUp: Starting")
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}