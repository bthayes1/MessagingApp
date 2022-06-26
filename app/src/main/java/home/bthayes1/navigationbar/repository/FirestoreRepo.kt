package home.bthayes1.navigationbar.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import home.bthayes1.navigationbar.models.MessageChannel
import home.bthayes1.navigationbar.models.User

internal class FirestoreRepo {
    private val db = Firebase.firestore(FirebaseApp.getInstance())

    private val userDataLiveData = MutableLiveData<User>()
    private val messageChannelLiveData = MutableLiveData<MessageChannel>()
    //
// query user data
// Delete user data
// Update User data
// Save new user data

    companion object{
        private const val TAG = "FirestoreRepo"
    }
    fun queryUserData(uid: String): DocumentReference {
        // Create a reference to the users collection
        val usersRef = db.collection("users")
        // Create a query against the collection.
        val userDocument = usersRef.document(uid)
        Log.i(TAG, userDocument.id)
        return userDocument
    }
}
