package com.kvcet.smartstudentportal.data.model

data class FeeRecord(
    val id: String = "",
    val studentId: String = "",
    val amountDue: Double = 0.0,
    val amountPaid: Double = 0.0,
    val status: String = "Pending", // "Paid" or "Pending"
    val receiptUrl: String = ""
)
