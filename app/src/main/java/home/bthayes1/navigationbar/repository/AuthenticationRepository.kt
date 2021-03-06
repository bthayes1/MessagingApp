package home.bthayes1.navigationbar.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser


interface AuthenticationRepository {
    fun getAuthMessage() : MutableLiveData<String>

    fun getLoggedInStatus(): MutableLiveData<Boolean>

    fun getUserData(): MutableLiveData<FirebaseUser?>

    fun register(email: String, pass: String, username: String)

    fun login(email: String, pass: String)

    fun signOut()
}