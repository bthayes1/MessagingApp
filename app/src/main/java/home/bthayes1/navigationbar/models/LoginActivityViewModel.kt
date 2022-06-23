package home.bthayes1.navigationbar.models

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import home.bthayes1.navigationbar.repository.FirebaseAuthUtil
import java.util.regex.Pattern


class LoginActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FirebaseAuthUtil
    private val userData: MutableLiveData<FirebaseUser?>
    private val loggedStatus: MutableLiveData<Boolean>
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    private val passwordAcceptable = MutableLiveData<Boolean>()

    companion object{
        private const val TAG = "LoginActivityViewModel"
    }
    init {
        repository = FirebaseAuthUtil(application)
        userData = repository.getUserData()
        loggedStatus = repository.getLoggedInStatus()
        Log.i(TAG, "Logged Status: ${loggedStatus.value}")
        email.value = ""
        password.value = ""
        username.value = ""
        name.value = ""
        passwordAcceptable.postValue(true)
    }
    fun getPasswordAcceptable(): MutableLiveData<Boolean> {
        Log.i(TAG, "getPasswordAcceptable()" )
        return passwordAcceptable
    }
    fun getLoggedStatus(): MutableLiveData<Boolean> {
        return loggedStatus
    }

    private fun register(email: String?, pass: String?) {
        repository.register(email, pass)
    }

    private fun signIn(email: String?, pass: String?) {
        repository.login(email, pass)
    }

    fun signOut() {
        repository.signOut()
    }

    fun checkSignInEntries(){
        if (email.value == "" || password.value == ""){
            Log.i(TAG, "CheckSignInEntries: Failed")
            showMessage("All entries must be filled")
            return
        }
        Log.i(TAG, "CheckSignInEntries: Success")
        signIn(email.value, password.value)
        Log.i(TAG, "Logged Status: ${loggedStatus.value}")
    }

    fun checkSignUpEntries(){
        if (email.value == "" || !passwordCheck(password.value)||
            username.value == "" || name.value == ""){
            Log.i(TAG, "CheckSignUpEntries: Failed")
            showMessage("All entries must be filled")
            return
        }
        Log.i(TAG, "CheckSignUpEntries: Success")
        register(email.value, password.value)
        Log.i(TAG, "Logged Status: ${loggedStatus.value}")
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
            showMessage("Password is not valid")
            return false
        }
        passwordAcceptable.postValue(true)
        Log.i(TAG, "Everything looks good: ${passwordAcceptable.value}")
        return true
    }
    private fun showMessage(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}