package home.bthayes1.navigationbar

//import androidx.navigation.fragment.NavHostFragment.findNavController
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import home.bthayes1.navigationbar.databinding.FragmentSignUpBinding
import home.bthayes1.navigationbar.models.LoginActivityViewModel
import androidx.navigation.fragment.findNavController



class SignUpFragment : Fragment() {
    companion object{
        private const val TAG = "SignUpFragment"
    }

    private var binding : FragmentSignUpBinding? = null
    private val sharedViewModel: LoginActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        sharedViewModel.getLoggedStatus().observe(viewLifecycleOwner){loggedIn ->
            if (loggedIn){
                goToMessages()
            }
        }
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewModel = sharedViewModel
            tvPriorAccount.setOnClickListener { goToSignIn() }

            // Show password requirements if user inputs a weak password
            sharedViewModel.getPasswordAcceptable().observe(viewLifecycleOwner){passwordOk ->
                Log.i(TAG, "passwordAcceptable: $passwordOk")
                if (passwordOk){
                    tvPasswordHint.visibility = View.GONE
                }else{
                    tvPasswordHint.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun goToMessages() {
        Log.i(TAG, "goToMainActivity: Starting")
        findNavController().navigate(R.id.action_signUpFragment_to_messageFragment)
    }

    private fun goToSignIn(){
        Log.i(TAG, "goToSignIn: Starting")
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
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