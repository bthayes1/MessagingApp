package home.bthayes1.navigationbar.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import home.bthayes1.navigationbar.models.User
import home.bthayes1.navigationbar.repository.AuthenticationRepository
import home.bthayes1.navigationbar.repository.FirestoreRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val firestoreRepoImpl : FirestoreRepository
) : ViewModel() {
    private var userLiveData = MutableLiveData<User>()

    init {
        Log.i(TAG, "Query users...")
        getUserData()
    }

    private fun getUserData() {
        // Query firestore and get data from user document
        val currentUser = authenticationRepository.getUserData()
        Log.i(TAG, currentUser.value?.uid ?: "No UID found")
        firestoreRepoImpl.queryUserData(currentUser.value!!.uid)
        userLiveData = firestoreRepoImpl.getUser()
        Log.i(TAG, "${userLiveData.value}")
    }

    fun getUserLiveData() : MutableLiveData<User>{
        return userLiveData
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}