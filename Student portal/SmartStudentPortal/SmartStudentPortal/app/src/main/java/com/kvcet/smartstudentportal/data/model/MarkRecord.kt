package com.kvcet.smartstudentportal.data.model

data class MarkRecord(
    val id: String = "",
    val studentId: String = "",
    val subject: String = "",
    val examType: String = "",   // e.g. "Internal 1", "Internal 2", "Model Exam"
    val marksScored: Double = 0.0,
    val maxMarks: Double = 100.0
)
