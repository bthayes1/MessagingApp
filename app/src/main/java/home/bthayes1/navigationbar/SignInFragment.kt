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
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "SignInFragment"


/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class SignInFragment : Fragment() {

    private lateinit var listener: OnItemSelectedListener
    private lateinit var btnLogin : Button
    private lateinit var tvRegister : TextView
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var clContainer : ConstraintLayout


    interface OnItemSelectedListener{
        fun signInSelected(email: String, password: String)
        fun registerSelected()
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
        val view =inflater.inflate(R.layout.fragment_sign_in, container, false)
        clContainer = view.findViewById(R.id.container)
        btnLogin = view.findViewById(R.id.login)
        tvRegister = view.findViewById(R.id.tvSignUp)
        etEmail = view.findViewById(R.id.etNewUsername)
        etPassword = view.findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
            if(etPassword.text.trim().isNotEmpty() && etEmail.text.trim().isNotEmpty()){
                onClickEvent(btnLogin)
            }else{
                Toast.makeText(clContainer.context, "All fields must be entered", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        tvRegister.setOnClickListener {onClickEvent(tvRegister)}
        return view
    }

    private fun onClickEvent(view: View) {
        when(view){
            btnLogin -> listener.signInSelected(etEmail.text.toString(), etPassword.text.toString())
            tvRegister -> listener.registerSelected()
        }
    }

}