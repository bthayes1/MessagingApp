package home.bthayes1.navigationbar

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var bottomSheetLayout : LinearLayout
    private lateinit var imgEdit : ImageView
    private lateinit var rvUsers : RecyclerView
    private lateinit var userList : MutableList<User>
    private lateinit var btnLogin : Button
    private lateinit var btnSignOut : Button
    private lateinit var etNewName : EditText
    private lateinit var etNewEmail : EditText
    private lateinit var ivPicture : CircleImageView
    private lateinit var cam_uri: Uri
    private var currentUserPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initViews()
        userList = mutableListOf()
        recyclerViewSetup()


        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        imgEdit.setOnClickListener {
            when (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                true -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                false -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        btnLogin.setOnClickListener{
            if (etNewName.text.trim().isNotEmpty() && etNewEmail.text.trim().isNotEmpty()){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                val newUser = User(etNewName.text.toString(), // Profile will have generic profile pic
                    etNewEmail.text.toString(),
                    null)
                userList.add(newUser)
                rvUsers.adapter?.notifyItemInserted(userList.size - 1)
                etNewName.text.clear()
                etNewEmail.text.clear()
            }
            else{
                Toast.makeText(this, "Must Enter Email and Password", Toast.LENGTH_SHORT).show()
            }
        }
        btnSignOut.setOnClickListener {
            Log.e(TAG, "Logging Out")
            FirebaseAuth.getInstance().signOut()

            val user = Firebase.auth.currentUser
            if (user != null){
                Log.e(TAG, "Log out failure")
            } else{
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }


        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // do stuff when the drawer is expanded
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // do stuff when the drawer is collapsed
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do stuff during the actual drag event for example
                // animating a background color change based on the offset

                // or for example hidding or showing a fab
                if (slideOffset > 0.2) {
//                    if (fab.isShown()) {
//                        fab.hide()
//                    }
                } else if (slideOffset < 0.15) {
//                    if (!fab.isShown()) {
//                        fab.show()
//                    }
                }
            }
        })
    }

    private fun recyclerViewSetup() {
        val adapter = UserAdapter(this, userList, object : UserAdapter.imageClickListener{
            override fun onImageClick(position: Int) {
                currentUserPosition = position
                dispatchTakePictureIntent()
            }
        })
        rvUsers.adapter = adapter
        rvUsers.layoutManager = LinearLayoutManager(this)
    }
    private fun initViews(){
        rvUsers = findViewById(R.id.rvUsers)
        bottomSheetLayout = findViewById(R.id.bottom_sheet_layout)
        imgEdit = findViewById(R.id.bottom_sheet_edit)
        btnLogin = findViewById(R.id.btnLogin)
        etNewName = findViewById(R.id.etNewName)
        etNewEmail = findViewById(R.id.etNewEmail)
        btnSignOut = findViewById(R.id.btnSignOut)
    }

    private fun dispatchTakePictureIntent() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Do you want to add a Profile Picture")
            .setPositiveButton("Yes", null)
            .setNegativeButton("No", null)
            .show()

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {

            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            cam_uri = this.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )!!
            Log.e(TAG, "dispatchTakePictureIntent: Position: $currentUserPosition")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri)
            try {
                cameraLauncherResult.launch(cameraIntent)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
                Log.e(TAG, e.toString())
            }
            dialog.dismiss()
        }
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener {dialog.dismiss()}
    }
    private val cameraLauncherResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val results = result.data
            // If the user comes back to this activity from EditActivity
            // with no error or cancellation
            Log.i(TAG, "cameraLauncherResult: Back from taking photo, Position of Item: $currentUserPosition")
            val currentUser = userList[currentUserPosition]
            currentUser.uri = cam_uri
            rvUsers.adapter?.notifyItemChanged(currentUserPosition)
        }
    }
}