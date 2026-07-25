package com.kvcet.smartstudentportal.data.model

data class LeaveRequestItem(
    val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val reason: String = "",
    val status: String = "Pending" // "Pending", "Approved", "Rejected"
)
