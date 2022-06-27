package home.bthayes1.navigationbar.presentation.messages

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import home.bthayes1.navigationbar.models.MessageChannel
import home.bthayes1.navigationbar.models.User
import home.bthayes1.navigationbar.repository.AuthenticationRepoImpl
import home.bthayes1.navigationbar.repository.FirestoreRepoImpl

class MessagesViewModel : ViewModel() {
    /**
     * live data needed:
     *  firebaseAuth repo
     *  firestore repo
     *  -List of message channels
     *  -list of contacts
     *  -User data class
     */
    private val repositoryAuth = AuthenticationRepoImpl()
    private val firestoreRepoImpl = FirestoreRepoImpl()
    private val userDocument = MutableLiveData<DocumentReference>()
    private val messageChannels = MutableLiveData<List<MessageChannel>>()
    private val User = MutableLiveData<User>()

    companion object{
        private const val TAG = "MessagesViewModel"
    }

    init {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null){
            val user = User(
                email = currentUser.email!!,
                username = currentUser.displayName!!,
                profilePic = null,
                uid = currentUser.uid
            )
            User.value = user
            userDocument.value = firestoreRepoImpl.queryUserData(user.uid)
        } else { Log.wtf(TAG, "Why did this happen")}
    }

    fun signout(){
        repositoryAuth.signOut()
    }

    fun getLoggedIn(): MutableLiveData<Boolean> {
        return repositoryAuth.getLoggedInStatus()
    }
    // find the ui from firebase auth
    // query data from firestore
    // if user is not in firestore (new user), add to firestore
    // load contacts
    // load message channels
    // load messages from message channels
    // update user info
    // add new messages
    // add new contacts
    // sign out


}