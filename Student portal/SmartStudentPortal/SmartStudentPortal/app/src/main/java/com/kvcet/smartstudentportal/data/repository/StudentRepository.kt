package com.kvcet.smartstudentportal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kvcet.smartstudentportal.data.model.AttendanceRecord
import com.kvcet.smartstudentportal.data.model.FeeRecord
import com.kvcet.smartstudentportal.data.model.MarkRecord
import com.kvcet.smartstudentportal.data.model.Student
import kotlinx.coroutines.tasks.await

/**
 * All reads/writes for the CSE-A prototype roster. Admin has write access to every
 * method here; students only ever call the "for a single studentId" reads, and
 * Firestore security rules (see README) enforce that the studentId matches their own uid.
 */
class StudentRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val students = db.collection("students")
    private val attendance = db.collection("attendance")
    private val marks = db.collection("marks")
    private val fees = db.collection("fees")

    // ---- Student roster (admin) ----

    suspend fun getAllStudents(): List<Student> {
        return students.get().await().documents.mapNotNull { doc ->
            doc.toObject(Student::class.java)?.copy(studentId = doc.id)
        }
    }

    suspend fun getStudent(studentId: String): Student? {
        val doc = students.document(studentId).get().await()
        return doc.toObject(Student::class.java)?.copy(studentId = doc.id)
    }

    suspend fun addStudent(student: Student): Result<String> {
        return try {
            val ref = students.document()
            ref.set(student.copy(studentId = ref.id)).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateStudent(student: Student): Result<Unit> {
        return try {
            students.document(student.studentId).set(student).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteStudent(studentId: String): Result<Unit> {
        return try {
            students.document(studentId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---- Attendance ----

    suspend fun getAttendanceFor(studentId: String): List<AttendanceRecord> {
        return attendance.whereEqualTo("studentId", studentId).get().await()
            .documents.mapNotNull { it.toObject(AttendanceRecord::class.java)?.copy(id = it.id) }
    }

    suspend fun markAttendance(record: AttendanceRecord): Result<Unit> {
        return try {
            val ref = if (record.id.isBlank()) attendance.document() else attendance.document(record.id)
            ref.set(record.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun attendancePercentage(records: List<AttendanceRecord>): Double {
        if (records.isEmpty()) return 0.0
        val present = records.count { it.status == "Present" }
        return (present.toDouble() / records.size) * 100.0
    }

    // ---- Marks ----

    suspend fun getMarksFor(studentId: String): List<MarkRecord> {
        return marks.whereEqualTo("studentId", studentId).get().await()
            .documents.mapNotNull { it.toObject(MarkRecord::class.java)?.copy(id = it.id) }
    }

    suspend fun addOrUpdateMark(mark: MarkRecord): Result<Unit> {
        return try {
            val ref = if (mark.id.isBlank()) marks.document() else marks.document(mark.id)
            ref.set(mark.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---- Fees ----

    suspend fun getFeeFor(studentId: String): FeeRecord? {
        val snapshot = fees.whereEqualTo("studentId", studentId).limit(1).get().await()
        return snapshot.documents.firstOrNull()?.let {
            it.toObject(FeeRecord::class.java)?.copy(id = it.id)
        }
    }

    suspend fun setFee(fee: FeeRecord): Result<Unit> {
        return try {
            val ref = if (fee.id.isBlank()) fees.document() else fees.document(fee.id)
            ref.set(fee.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
