package com.kvcet.smartstudentportal.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kvcet.smartstudentportal.data.model.Student

@Composable
fun StudentListScreen(
    viewModel: AdminViewModel,
    onAddStudent: () -> Unit,
    onEditStudent: (Student) -> Unit
) {
    val students by viewModel.students.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadStudents() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddStudent) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("CSE-A roster (${students.size})", style = MaterialTheme.typography.titleMedium)

            if (isLoading) {
                CircularProgressIndicator()
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(students) { student ->
                    Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(student.name, style = MaterialTheme.typography.titleSmall)
                            Text("Reg no: ${student.registerNumber} · Roll no: ${student.rollNumber}")
                            Text("Mobile: ${student.mobileNumber}")
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(onClick = { onEditStudent(student) }) {
                                    Text("Edit")
                                }
                                Button(onClick = { viewModel.deleteStudent(student.studentId) }) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
