package home.bthayes1.navigationbar.presentation.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import home.bthayes1.navigationbar.R
import home.bthayes1.navigationbar.databinding.FragmentMessageBinding
import home.bthayes1.navigationbar.databinding.FragmentProfileBinding
import home.bthayes1.navigationbar.models.User
import home.bthayes1.navigationbar.presentation.messages.MessageFragment
import home.bthayes1.navigationbar.presentation.messages.MessagesViewModel


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var binding : FragmentProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var currentUser : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentProfileBinding.inflate(inflater, container, false)
        binding = fragmentBinding



        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = profileViewModel
            profileViewModel.getUserLiveData().observe(viewLifecycleOwner){ user ->
                Log.i(TAG, "$user")
                if(user != null) {
                    currentUser = user
                    tvProfileEmail.text = currentUser.email
                    tvProfileUsername.text = currentUser.username
                    //tvProfileProfilePic.text = currentUser.profilePic
                    tvProfileName.text = currentUser.name
                }
            }
        }
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}