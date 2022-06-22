package home.bthayes1.navigationbar.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


internal class FirebaseAuthUtil(private val application: Application) {
    private val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    companion object{
        private const val TAG = "FirebaseAuthUtil"
    }

    init {
        if (auth.currentUser != null) {
            Log.i(TAG, "User Logged In Already")
            userLoggedMutableLiveData.postValue(true)
            firebaseUserMutableLiveData.postValue(auth.currentUser)
        }
        else{
            userLoggedMutableLiveData.postValue(false)
        }
    }

    fun getLoggedInStatus(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }

    fun getUserData(): MutableLiveData<FirebaseUser?> {
        return firebaseUserMutableLiveData
    }

    fun register(email: String?, pass: String?) {
        auth.createUserWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUserMutableLiveData.postValue(auth.currentUser)
                userLoggedMutableLiveData.postValue(true)
                Toast.makeText(application, "User Created Successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(application, task.exception?.message ?: "Unknown Error", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun login(email: String?, pass: String?) {
        auth.signInWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUserMutableLiveData.postValue(auth.currentUser)
                userLoggedMutableLiveData.postValue(true)
                Toast.makeText(application, "User Login Success", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(application, task.exception?.message ?: "Unknown Error", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun signOut() {
        auth.signOut()
        userLoggedMutableLiveData.postValue(false)
    }
}