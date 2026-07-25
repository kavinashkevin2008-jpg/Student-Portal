package com.kvcet.smartstudentportal.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminDashboardScreen(
    onOpenStudents: () -> Unit,
    onOpenNotices: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Admin dashboard", style = MaterialTheme.typography.headlineSmall)
        Text("KVCET · CSE · Section A", style = MaterialTheme.typography.bodyMedium)

        Button(onClick = onOpenStudents, modifier = Modifier.fillMaxWidth()) {
            Text("Manage students")
        }
        Button(onClick = onOpenNotices, modifier = Modifier.fillMaxWidth()) {
            Text("Notice board")
        }
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Log out")
        }
    }
}
