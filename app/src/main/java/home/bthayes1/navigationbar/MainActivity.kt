package home.bthayes1.navigationbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var btnSignout : Button
    private lateinit var tvHello : TextView
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        tvHello.text = "${currentUser?.displayName}, ${currentUser?.email}"

        btnSignout.setOnClickListener {
            Firebase.auth.signOut()
            returnToLogin()
        }
    }

    private fun returnToLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun initViews() {
        btnSignout = findViewById(R.id.btnSignout)
        tvHello = findViewById(R.id.tvHello)
    }

}