package com.kvcet.smartstudentportal.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvcet.smartstudentportal.data.model.Student
import com.kvcet.smartstudentportal.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: StudentRepository = StudentRepository()
) : ViewModel() {

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadStudents() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _students.value = repository.getAllStudents()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveStudent(student: Student, onDone: () -> Unit) {
        viewModelScope.launch {
            val result = if (student.studentId.isBlank()) {
                repository.addStudent(student)
            } else {
                repository.updateStudent(student).map { student.studentId }
            }
            result.fold(
                onSuccess = {
                    loadStudents()
                    onDone()
                },
                onFailure = { _errorMessage.value = it.message }
            )
        }
    }

    fun deleteStudent(studentId: String) {
        viewModelScope.launch {
            repository.deleteStudent(studentId)
            loadStudents()
        }
    }
}
