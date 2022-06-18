package home.bthayes1.navigationbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), SignInFragment.OnItemSelectedListener, SignUpFragment.OnItemSelectedListener{
    private val auth = FirebaseAuth.getInstance()
    private val signInFragment = SignInFragment()
    private val signUpFragment = SignUpFragment()


    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if(currentUser != null){
            //User is already signed in... go to MainActivity
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        openFragment(signInFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentLogin, fragment)
        ft.commit()
    }

    override fun signInSelected(email : String, password : String) {
        Log.e(TAG, "signInSelected: $email, $password")
        //Login with email and password to firebase
        loginAccount(email, password)
        //If user is not found, go to registration
        //If password is incorrect, display incorrect password
    }

    private fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    launchMainActivity()
                } else {
                    val error : String? = task.exception?.message
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, error,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun launchMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun registerSelected() {
        Log.e(TAG, "registerSelected")
        //Need to take email and password to this fragment
        openFragment(signUpFragment)
    }

    override fun signUpSelected(userInfo: List<String>) {
        createAccount(userInfo)
    }

    private fun createAccount(userInfo: List<String>) {
        val email = userInfo[0]
        val password = userInfo[1]
        val name = userInfo[2]
        val username = userInfo[3]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    //TODO: Add name and username to user profile
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createAccount: User info: ${user.displayName}, ${user.email}, $password")
                                Toast.makeText(this, "User successfully created", Toast.LENGTH_SHORT)
                                    .show()
                                launchMainActivity()
                            }else{
                                Log.e(TAG, "User update failed")
                            }
                        }

                    //TODO: Go to main activity
                } else {
                   val error = task.exception.toString()
                   Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun showError(error: Exception?){

        when (error.toString()) {
            "ERROR_INVALID_CUSTOM_TOKEN" -> {
                Toast.makeText(
                    this,
                    "The custom token format is incorrect. Please check the documentation.",
                    Toast.LENGTH_LONG
                ).show()
            }
            "ERROR_CUSTOM_TOKEN_MISMATCH" -> Toast.makeText(
                this,
                "The custom token corresponds to a different audience.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_INVALID_CREDENTIAL" -> Toast.makeText(
                this,
                "The supplied auth credential is malformed or has expired.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(
                    this,
                    "The email address is badly formatted.",
                    Toast.LENGTH_LONG
                ).show()
//                etEmail.setError("The email address is badly formatted.")
//                etEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(
                    this,
                    "The password is invalid",
                    Toast.LENGTH_LONG
                ).show()
//                etPassword.setError("password is incorrect ")
//                etPassword.requestFocus()
//                etPassword.setText("")
            }
            "ERROR_USER_MISMATCH" -> Toast.makeText(
                this,
                "The supplied credentials do not correspond to the previously signed in user.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_REQUIRES_RECENT_LOGIN" -> Toast.makeText(
                this,
                "This operation is sensitive and requires recent authentication. Log in again before retrying this request.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> Toast.makeText(
                this,
                "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(
                    this,
                    "The email address is already in use by another account.   ",
                    Toast.LENGTH_LONG
                ).show()
//                etEmail.setError("The email address is already in use by another account.")
//                etEmail.requestFocus()
            }
            "ERROR_CREDENTIAL_ALREADY_IN_USE" -> Toast.makeText(
                this,
                "This credential is already associated with a different user account.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_USER_DISABLED" -> Toast.makeText(
                this,
                "The user account has been disabled by an administrator.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_USER_TOKEN_EXPIRED" -> Toast.makeText(
                this,
                "The user's credential is no longer valid. The user must sign in again.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_USER_NOT_FOUND" -> Toast.makeText(
                this,
                "There is no user record corresponding to this identifier. The user may have been deleted.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_INVALID_USER_TOKEN" -> Toast.makeText(
                this,
                "The user's credential is no longer valid. The user must sign in again.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_OPERATION_NOT_ALLOWED" -> Toast.makeText(
                this,
                "This operation is not allowed. You must enable this service in the console.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(
                    this,
                    "The given password is invalid.",
                    Toast.LENGTH_LONG
                ).show()
//                etPassword.setError("The password is invalid it must 6 characters at least")
//                etPassword.requestFocus()
            }
        }
    }
    companion object{
        private const val TAG = "LoginActivity"
    }
}
