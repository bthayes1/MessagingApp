package home.bthayes1.navigationbar

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class SignUpFragment : Fragment() {
    private lateinit var listener: OnItemSelectedListener
    private lateinit var btnSignUp : Button
    private lateinit var etNewEmail : EditText
    private lateinit var etNewPassword : EditText
    private lateinit var etNewName : EditText
    private lateinit var etNewUsername : EditText
    private lateinit var clContainer : ConstraintLayout
    private lateinit var infoList: List<String>

    interface OnItemSelectedListener{
        fun signUpSelected(list : List<String>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "$context")
        listener = if (context is OnItemSelectedListener) {
            context
        } else {
            throw ClassCastException("$context must implement MyListFragment.OnItemSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_sign_up, container, false)
        initViews(view)
        btnSignUp.setOnClickListener {
            if(textEntered(etNewEmail) && textEntered(etNewPassword) &&
                textEntered(etNewName) && textEntered(etNewUsername))
            {
                val email = etNewEmail.text.toString()
                val password = etNewPassword.text.toString()
                val name = etNewName.text.toString()
                val username = etNewUsername.text.toString()
                infoList = listOf(email, password, name, username)
                onClickEvent(btnSignUp)
            }
            else{
                Toast.makeText(clContainer.context, "All fields must be entered", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }

    private fun initViews(view: View) {
        clContainer = view.findViewById(R.id.container)
        btnSignUp = view.findViewById(R.id.btnSignup)
        etNewEmail = view.findViewById(R.id.etNewEmail)
        etNewName = view.findViewById(R.id.etNewName)
        etNewPassword = view.findViewById(R.id.etNewPassword)
        etNewUsername = view.findViewById(R.id.etNewUsername)
    }

    private fun textEntered(editText: EditText): Boolean {
        return editText.text.trim().isNotEmpty()
    }

    private fun onClickEvent(view: View) {
        when(view){
            btnSignUp-> listener.signUpSelected(infoList)
        }
    }

    companion object{
        private const val TAG = "SignUpFragment"
    }

}