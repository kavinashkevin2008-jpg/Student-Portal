package com.kvcet.smartstudentportal.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StudentDashboardScreen(
    studentId: String,
    viewModel: StudentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onOpenNotices: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(studentId) { viewModel.load(studentId) }

    if (state.isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(24.dp))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(state.student?.name ?: "Student", style = MaterialTheme.typography.headlineSmall)
        Text("Reg no: ${state.student?.registerNumber ?: "-"}", style = MaterialTheme.typography.bodyMedium)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Attendance", style = MaterialTheme.typography.titleSmall)
                Text("${"%.1f".format(state.attendancePercent)}%")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Internal marks", style = MaterialTheme.typography.titleSmall)
                if (state.marks.isEmpty()) {
                    Text("No marks published yet")
                } else {
                    state.marks.forEach { mark ->
                        Text("${mark.subject} (${mark.examType}): ${mark.marksScored}/${mark.maxMarks}")
                    }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Fee status", style = MaterialTheme.typography.titleSmall)
                val fee = state.fee
                if (fee == null) {
                    Text("No fee record found")
                } else {
                    Text("Status: ${fee.status}")
                    Text("Paid: ${fee.amountPaid} / Due: ${fee.amountDue}")
                }
            }
        }

        Button(onClick = onOpenNotices, modifier = Modifier.fillMaxWidth()) {
            Text("Notice board")
        }
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Log out")
        }
    }
}
