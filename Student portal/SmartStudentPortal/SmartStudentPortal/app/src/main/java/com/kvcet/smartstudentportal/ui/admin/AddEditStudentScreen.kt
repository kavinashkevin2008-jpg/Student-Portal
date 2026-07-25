package com.kvcet.smartstudentportal.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kvcet.smartstudentportal.data.model.Student

@Composable
fun AddEditStudentScreen(
    viewModel: AdminViewModel,
    existingStudent: Student?,
    onDone: () -> Unit
) {
    var name by remember { mutableStateOf(existingStudent?.name ?: "") }
    var regNo by remember { mutableStateOf(existingStudent?.registerNumber ?: "") }
    var rollNo by remember { mutableStateOf(existingStudent?.rollNumber ?: "") }
    var year by remember { mutableStateOf(existingStudent?.year ?: "") }
    var semester by remember { mutableStateOf(existingStudent?.semester ?: "") }
    var mobile by remember { mutableStateOf(existingStudent?.mobileNumber ?: "") }
    var email by remember { mutableStateOf(existingStudent?.email ?: "") }
    var parentName by remember { mutableStateOf(existingStudent?.parentName ?: "") }
    var parentMobile by remember { mutableStateOf(existingStudent?.parentMobile ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            if (existingStudent == null) "Add student" else "Edit student",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = regNo, onValueChange = { regNo = it }, label = { Text("Register number") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = rollNo, onValueChange = { rollNo = it }, label = { Text("Roll number") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Year") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = semester, onValueChange = { semester = it }, label = { Text("Semester") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile number") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = parentName, onValueChange = { parentName = it }, label = { Text("Parent name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = parentMobile, onValueChange = { parentMobile = it }, label = { Text("Parent mobile") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val student = (existingStudent ?: Student()).copy(
                    name = name,
                    registerNumber = regNo,
                    rollNumber = rollNo,
                    year = year,
                    semester = semester,
                    mobileNumber = mobile,
                    email = email,
                    parentName = parentName,
                    parentMobile = parentMobile
                )
                viewModel.saveStudent(student, onDone)
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Save")
        }
    }
}
