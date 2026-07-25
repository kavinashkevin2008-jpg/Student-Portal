package com.kvcet.smartstudentportal.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvcet.smartstudentportal.data.model.AttendanceRecord
import com.kvcet.smartstudentportal.data.model.FeeRecord
import com.kvcet.smartstudentportal.data.model.MarkRecord
import com.kvcet.smartstudentportal.data.model.Student
import com.kvcet.smartstudentportal.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StudentDashboardState(
    val student: Student? = null,
    val attendancePercent: Double = 0.0,
    val marks: List<MarkRecord> = emptyList(),
    val fee: FeeRecord? = null,
    val isLoading: Boolean = true
)

class StudentViewModel(
    private val repository: StudentRepository = StudentRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(StudentDashboardState())
    val state: StateFlow<StudentDashboardState> = _state

    fun load(studentId: String) {
        viewModelScope.launch {
            val student = repository.getStudent(studentId)
            val attendance: List<AttendanceRecord> = repository.getAttendanceFor(studentId)
            val marks = repository.getMarksFor(studentId)
            val fee = repository.getFeeFor(studentId)

            _state.value = StudentDashboardState(
                student = student,
                attendancePercent = repository.attendancePercentage(attendance),
                marks = marks,
                fee = fee,
                isLoading = false
            )
        }
    }
}
