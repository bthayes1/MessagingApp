package home.bthayes1.navigationbar.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase


internal class AuthenticationRepoImpl : AuthenticationRepository{
    private val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private var functions: FirebaseFunctions = Firebase.functions
    private val userLoggedMutableLiveData = MutableLiveData<Boolean>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val authMessage: MutableLiveData<String> = MutableLiveData()
    companion object{
        private const val TAG = "AuthenticationRepoImpl"
    }

    init {
        if (auth.currentUser != null) {
            Log.i(TAG, "User Logged In Already")
            userLoggedMutableLiveData.postValue(true)
            firebaseUserMutableLiveData.value = auth.currentUser
            userLoggedMutableLiveData.value = checkIfUserLoggedIn()
        }
        else{
            Log.i(TAG, "I am here")
            userLoggedMutableLiveData.value = checkIfUserLoggedIn()
        }
    }

    override fun getAuthMessage() : MutableLiveData<String>{
        return authMessage
    }

    override fun getLoggedInStatus(): MutableLiveData<Boolean> {
        Log.i(TAG, "getLoggedInStatus: ${userLoggedMutableLiveData.value}")
        return userLoggedMutableLiveData
    }

    override fun getUserData(): MutableLiveData<FirebaseUser?> {
        return firebaseUserMutableLiveData
    }

    override fun register(email: String, pass: String, username: String, name: String) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }
                auth.currentUser!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        val currentUser = auth.currentUser!!
                        if(task.isSuccessful){
                            val userData = hashMapOf(
                                "email" to currentUser.email,
                                "username" to currentUser.displayName,
                                "profile_pic_url" to currentUser.photoUrl.toString(),
                                "name" to name,
                                "uid" to currentUser.uid
                            )
                            val result = addUser(userData)
                            firebaseUserMutableLiveData.postValue(auth.currentUser)
                            userLoggedMutableLiveData.value = checkIfUserLoggedIn()
                            authMessage.postValue(result.exception?.message ?: "User Created Successfully")

                        }else{
                            authMessage.postValue(task.exception?.message ?: "Unknown Error")
                        }
                    }
            } else {
                authMessage.postValue(task.exception?.message ?: "Unknown Error")
            }
        }
    }

    override fun addUser(userData: HashMap<String, String?>): Task<String> {
        Log.i(TAG, "Adding user... $userData")
        return functions
            .getHttpsCallable("addNewUser")
            .call(userData)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

    private fun printNewInfo(){
        Log.i(TAG, "printNewInfo")
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // UID specific to the provider
                val uid = profile.uid
                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                Log.i(TAG, "User created: $uid, $name, $email")
            }
        }
    }

    override fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUserMutableLiveData.postValue(auth.currentUser)
                userLoggedMutableLiveData.value = checkIfUserLoggedIn()
                authMessage.postValue("Success")
            } else {
                authMessage.postValue(task.exception?.message ?: "Unknown Error")
            }
        }
    }
    private fun checkIfUserLoggedIn(): Boolean{
        return auth.currentUser != null
    }

    override fun signOut() {
        auth.signOut()
        userLoggedMutableLiveData.value = checkIfUserLoggedIn()
        Log.i(TAG, auth.uid ?: "User is logged out: userLogged ${userLoggedMutableLiveData.value}")
    }
}