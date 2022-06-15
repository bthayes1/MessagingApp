package home.bthayes1.navigationbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var btnSignIn: SignInButton
    private lateinit var etEnterEmail : EditText
    private lateinit var etEnterPassword : EditText
    private lateinit var btnSignUp : Button

    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if(currentUser != null){
            launchMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        btnSignIn = findViewById(R.id.btnSignIn)
        etEnterEmail = findViewById(R.id.etEnterEmail)
        etEnterPassword = findViewById(R.id.etEnterPassword)
        btnSignUp = findViewById(R.id.btnSignUp)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(this, gso)
        btnSignIn.setOnClickListener {
            val signInIntent = client.signInIntent
            signInActivityLauncher.launch(signInIntent)
        }
        btnSignUp.setOnClickListener {
            val email = etEnterEmail.text.toString()
            val password = etEnterPassword.text.toString()
            Log.e(TAG, email + password)
            if (email.trim().isNotEmpty() && email.trim().isNotEmpty()){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            showUserInfo(user)
                            launchMainActivity()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun showUserInfo(user: FirebaseUser?) {
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
            Log.e(TAG, "User Info: Name: $name, Email: $email,Photo: $photoUrl, Email Verified: $emailVerified,UID: $uid")
        } ?: kotlin.run {
            Toast.makeText(this, "Google Login Failure", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "User is Null")
        }
    }

    private val signInActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            val user = Firebase.auth.currentUser

            if (user != null) {
                launchMainActivity()
            } else {
                Log.e(TAG, "No user found")
            }
        }
    }

    private fun launchMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
