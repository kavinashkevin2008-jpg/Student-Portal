package com.kvcet.smartstudentportal.data.model

// Role is stored on the Firebase Auth user's document in the "users" collection.
// "admin" can manage everything; "student" can only read their own linked Student doc.
data class UserAccount(
    val uid: String = "",
    val email: String = "",
    val role: String = "student", // "admin" or "student"
    val studentId: String = ""    // filled in only when role == "student"
)
