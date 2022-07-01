package home.bthayes1.navigationbar.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import home.bthayes1.navigationbar.models.User
import java.util.*

interface FirestoreRepository {

    fun queryUserData(uid: String)

    fun getAllUsers()

    fun getUser(): MutableLiveData<User>

}