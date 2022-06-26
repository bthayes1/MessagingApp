package home.bthayes1.navigationbar.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase


internal class FirebaseAuthUtil {
    private val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val userLoggedMutableLiveData = MutableLiveData<Boolean>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val authMessage: MutableLiveData<String> = MutableLiveData()
    companion object{
        private const val TAG = "FirebaseAuthUtil"
    }

    init {
        if (auth.currentUser != null) {
            Log.i(TAG, "User Logged In Already")
            userLoggedMutableLiveData.postValue(true)
            userLoggedMutableLiveData.value = checkIfUserLoggedIn()
        }
        else{
            Log.i(TAG, "I am here")
            userLoggedMutableLiveData.value = checkIfUserLoggedIn()
        }
    }

    fun getAuthMessage() : MutableLiveData<String>{
        return authMessage
    }

    fun getLoggedInStatus(): MutableLiveData<Boolean> {
        Log.i(TAG, "getLoggedInStatus: ${userLoggedMutableLiveData.value}")
        return userLoggedMutableLiveData
    }

    fun getUserData(): MutableLiveData<FirebaseUser?> {
        return firebaseUserMutableLiveData
    }

    fun register(email: String, pass: String, username: String) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }
                auth.currentUser!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            printNewInfo()
                            firebaseUserMutableLiveData.postValue(auth.currentUser)
                            userLoggedMutableLiveData.value = checkIfUserLoggedIn()
                            authMessage.postValue("User Created Successfully")

                        }else{
                            authMessage.postValue(task.exception?.message ?: "Unknown Error")
                        }
                    }
            } else {
                authMessage.postValue(task.exception?.message ?: "Unknown Error")
            }
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

    fun login(email: String, pass: String) {
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

    fun signOut() {
        auth.signOut()
        userLoggedMutableLiveData.value = checkIfUserLoggedIn()
        Log.i(TAG, auth.uid ?: "User is logged out: userLogged ${userLoggedMutableLiveData.value}")
    }
}