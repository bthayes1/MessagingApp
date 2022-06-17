package home.bthayes1.navigationbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity(), SignInFragment.OnItemSelectedListener, SignUpFragment.OnItemSelectedListener{
    private lateinit var auth: FirebaseAuth
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
        //If user is not found, go to registration
        //If password is incorrect, display incorrect password
    }

    override fun registerSelected() {
        Log.e(TAG, "registerSelected")
        //Need to take email and password to this fragment
        openFragment(signUpFragment)
    }

    companion object{
        private const val TAG = "LoginActivity"
    }

    override fun signUpSelected(userInfo: List<String>) {
        Log.e(TAG, "signUpSelected: $userInfo") //list(email, pass, name, username)
        createAccount(userInfo[0], userInfo[1])
    }
    private fun createAccount(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    //TODO: Add name and username to user profile
                    //TODO: Go to main activity
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
