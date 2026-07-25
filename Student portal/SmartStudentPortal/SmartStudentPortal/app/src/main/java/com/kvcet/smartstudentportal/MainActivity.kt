package com.kvcet.smartstudentportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kvcet.smartstudentportal.ui.navigation.AppNavHost
import com.kvcet.smartstudentportal.ui.theme.SmartStudentPortalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartStudentPortalTheme {
                AppNavHost()
            }
        }
    }
}
