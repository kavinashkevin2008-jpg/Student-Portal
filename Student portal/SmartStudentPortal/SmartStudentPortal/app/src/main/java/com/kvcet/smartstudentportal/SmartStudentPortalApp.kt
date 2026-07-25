package com.kvcet.smartstudentportal

import android.app.Application
import com.google.firebase.FirebaseApp

class SmartStudentPortalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
