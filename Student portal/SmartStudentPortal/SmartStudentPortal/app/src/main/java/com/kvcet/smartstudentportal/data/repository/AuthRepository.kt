package com.kvcet.smartstudentportal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kvcet.smartstudentportal.data.model.UserAccount
import kotlinx.coroutines.tasks.await

/**
 * Wraps Firebase Auth + the "users" collection, which stores each account's role
 * (admin / student) and, for students, the linked student document id.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun login(email: String, password: String): Result<UserAccount> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Login failed"))
            val snapshot = db.collection("users").document(uid).get().await()
            val account = snapshot.toObject(UserAccount::class.java)
                ?: return Result.failure(Exception("No account record found for this user"))
            Result.success(account)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUid(): String? = auth.currentUser?.uid

    // Used by the admin to create a login for a student they've already added to the roster.
    suspend fun createStudentAccount(
        email: String,
        password: String,
        studentId: String
    ): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Account creation failed"))
            val account = UserAccount(uid = uid, email = email, role = "student", studentId = studentId)
            db.collection("users").document(uid).set(account).await()
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
