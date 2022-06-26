package home.bthayes1.navigationbar.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import home.bthayes1.navigationbar.repository.FirebaseAuthUtil
import java.util.regex.Pattern


class LoginActivityViewModel : ViewModel(){
    private val repositoryAuth: FirebaseAuthUtil = FirebaseAuthUtil()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    private var userLoggedStatus = repositoryAuth.getLoggedInStatus()
    private val passwordAcceptable = MutableLiveData<Boolean>()
    private val authMessage : MutableLiveData<String>

    companion object{
        private const val TAG = "LoginActivityViewModel"
    }
    init {
        authMessage = repositoryAuth.getAuthMessage()
        email.value = ""
        password.value = ""
        username.value = ""
        name.value = ""
        passwordAcceptable.postValue(true)
    }
    fun getMessage() : MutableLiveData<String>{
        return authMessage
    }
    fun getPasswordAcceptable(): MutableLiveData<Boolean> {
        Log.i(TAG, "getPasswordAcceptable()" )
        return passwordAcceptable
    }
    fun getLoggedStatus(): MutableLiveData<Boolean> {
        userLoggedStatus = repositoryAuth.getLoggedInStatus()
        Log.i(TAG, "userLoggedStatus : ${userLoggedStatus.value}")
        return userLoggedStatus
    }
    fun signOut(){
        repositoryAuth.signOut()
    }

    private fun register(email: String, pass: String, username: String) {
        repositoryAuth.register(email, pass, username)
    }

    private fun signIn(email: String, pass: String) {
        repositoryAuth.login(email, pass)
    }

    fun checkSignInEntries(){
        if (email.value == "" || password.value == ""){
            Log.i(TAG, "CheckSignInEntries: Failed")
            authMessage.postValue("All entries must be filled")
            return
        }
        Log.i(TAG, "CheckSignInEntries: Success")
        signIn(email.value!!, password.value!!)
       // Log.i(TAG, "Logged Status: ${loggedStatus.value}")
    }

    fun checkSignUpEntries(){
        if (email.value == "" || !passwordCheck(password.value)||
            username.value == "" || name.value == ""){
            Log.i(TAG, "CheckSignUpEntries: Failed")
            authMessage.postValue("All entries must be filled")
            return
        }
        Log.i(TAG, "CheckSignUpEntries: Success")
        register(email.value!!, password.value!!, username.value!!)
      //  Log.i(TAG, "Logged Status: ${loggedStatus.value}")
    }

    private fun passwordCheck(password: String?): Boolean {
        Log.i(TAG, "passwordCheck")
        val pattern : Pattern by lazy {
            Pattern.compile(
                "^" +
                        "(?=.*[@#$%^&+=_*!])" +  // at least 1 special character
                        "(?=.*[a-z])" +  // at least 1 lowercase
                        "(?=.*[A-Z])" +  // at least 1 uppercase
                        "(?=.*[0-9])" +  // at least 1 number
                        "(?=\\S+$)" +  // no white spaces
                        ".{6,}" +  // at least 6 characters
                        "$"
            )
        }
        check(pattern.matcher(password ?: "").matches()){
            passwordAcceptable.postValue(false)
            Log.i(TAG, "Password is not valid: ${passwordAcceptable.value}")
            authMessage.postValue("Password is not valid")
            return false
        }
        passwordAcceptable.postValue(true)
        Log.i(TAG, "Everything looks good: ${passwordAcceptable.value}")
        return true
    }

    fun signout() {
       repositoryAuth.signOut()
    }
}