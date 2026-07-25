package com.kvcet.smartstudentportal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kvcet.smartstudentportal.data.model.Student
import com.kvcet.smartstudentportal.ui.admin.AddEditStudentScreen
import com.kvcet.smartstudentportal.ui.admin.AdminDashboardScreen
import com.kvcet.smartstudentportal.ui.admin.AdminViewModel
import com.kvcet.smartstudentportal.ui.admin.StudentListScreen
import com.kvcet.smartstudentportal.ui.auth.AuthViewModel
import com.kvcet.smartstudentportal.ui.auth.LoginScreen
import com.kvcet.smartstudentportal.ui.notice.NoticeBoardScreen
import com.kvcet.smartstudentportal.ui.student.StudentDashboardScreen

private object Routes {
    const val LOGIN = "login"
    const val ADMIN_HOME = "admin_home"
    const val STUDENT_LIST = "student_list"
    const val ADD_STUDENT = "add_student"
    const val EDIT_STUDENT = "edit_student"
    const val ADMIN_NOTICES = "admin_notices"
    const val STUDENT_HOME = "student_home/{studentId}"
    const val STUDENT_NOTICES = "student_notices"
}

@Composable
fun AppNavHost() {
    val navController: NavHostController = rememberNavController()
    val authViewModel = AuthViewModel()
    val adminViewModel = AdminViewModel()

    // Holds the student picked from the roster for editing; Compose Navigation
    // doesn't pass full objects between destinations, so we stash it here instead
    // of re-fetching from Firestore on every navigation.
    var studentBeingEdited: Student? = null

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(viewModel = authViewModel) { account ->
                if (account.role == "admin") {
                    navController.navigate(Routes.ADMIN_HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                } else {
                    navController.navigate("student_home/${account.studentId}") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
        }

        composable(Routes.ADMIN_HOME) {
            AdminDashboardScreen(
                onOpenStudents = { navController.navigate(Routes.STUDENT_LIST) },
                onOpenNotices = { navController.navigate(Routes.ADMIN_NOTICES) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.STUDENT_LIST) {
            StudentListScreen(
                viewModel = adminViewModel,
                onAddStudent = {
                    studentBeingEdited = null
                    navController.navigate(Routes.ADD_STUDENT)
                },
                onEditStudent = { student ->
                    studentBeingEdited = student
                    navController.navigate(Routes.EDIT_STUDENT)
                }
            )
        }

        composable(Routes.ADD_STUDENT) {
            AddEditStudentScreen(
                viewModel = adminViewModel,
                existingStudent = null,
                onDone = { navController.popBackStack() }
            )
        }

        composable(Routes.EDIT_STUDENT) {
            AddEditStudentScreen(
                viewModel = adminViewModel,
                existingStudent = studentBeingEdited,
                onDone = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_NOTICES) {
            NoticeBoardScreen(isAdmin = true, postedByName = "Admin")
        }

        composable(Routes.STUDENT_HOME) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            StudentDashboardScreen(
                studentId = studentId,
                onOpenNotices = { navController.navigate(Routes.STUDENT_NOTICES) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.STUDENT_NOTICES) {
            NoticeBoardScreen(isAdmin = false, postedByName = "")
        }
    }
}
