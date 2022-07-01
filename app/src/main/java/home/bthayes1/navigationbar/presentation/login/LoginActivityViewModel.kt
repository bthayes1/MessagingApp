package home.bthayes1.navigationbar.presentation.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import home.bthayes1.navigationbar.presentation.MainActivity
import home.bthayes1.navigationbar.repository.AuthenticationRepoImpl
import home.bthayes1.navigationbar.repository.AuthenticationRepository
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    private val repositoryAuth: AuthenticationRepository

)  : ViewModel(){
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
        Log.i(TAG,"onCreate: the app context: ${repositoryAuth}")
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

    private fun register() {
        repositoryAuth.register(email.value!!, password.value!!, username.value!!, name.value!!)
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
        register()
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