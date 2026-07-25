package com.kvcet.smartstudentportal.data.model

data class Student(
    val studentId: String = "",       // Firestore document id
    val uid: String = "",             // linked Firebase Auth uid, blank until account created
    val name: String = "",
    val registerNumber: String = "",
    val rollNumber: String = "",
    val department: String = "CSE",
    val section: String = "A",
    val year: String = "",
    val semester: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val parentName: String = "",
    val parentMobile: String = "",
    val photoUrl: String = ""
)
