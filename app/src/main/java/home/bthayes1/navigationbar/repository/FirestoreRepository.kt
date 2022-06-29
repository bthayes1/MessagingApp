package home.bthayes1.navigationbar.repository

import com.google.firebase.firestore.DocumentReference

interface FirestoreRepository {

    fun queryUserData(uid: String) : DocumentReference

    fun getAllUsers()

}