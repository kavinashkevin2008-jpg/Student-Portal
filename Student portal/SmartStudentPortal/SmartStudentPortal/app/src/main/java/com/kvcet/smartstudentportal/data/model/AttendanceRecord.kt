package com.kvcet.smartstudentportal.data.model

data class AttendanceRecord(
    val id: String = "",
    val studentId: String = "",
    val date: String = "",     // stored as "yyyy-MM-dd" for simple sorting/filtering
    val status: String = "Present" // "Present" or "Absent"
)
