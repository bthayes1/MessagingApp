package home.bthayes1.navigationbar.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import home.bthayes1.navigationbar.R
import home.bthayes1.navigationbar.presentation.login.LoginActivityViewModel

class MainActivity : AppCompatActivity(R.layout.activity_login) {
    companion object{
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav : BottomNavigationView = findViewById(R.id.bottom_nav)
        val model: LoginActivityViewModel by viewModels()
        model.getMessage().observe(this) { message ->
            Log.i(TAG, "getMessage: $message")
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment){
                supportActionBar?.hide()
                bottomNav.visibility = View.GONE
            } else {
                supportActionBar?.show()
                bottomNav.visibility = View.VISIBLE
            }
        }
        NavigationUI.setupWithNavController(bottomNav, navController)

        setupActionBarWithNavController(navController)
    }
}
