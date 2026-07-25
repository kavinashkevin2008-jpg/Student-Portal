# Smart Student Portal — Android prototype (KVCET, CSE-A)

Native Android app in Kotlin + Jetpack Compose, backed by Firebase (Auth + Firestore + Storage).
Covers the prototype scope: admin-managed roster, attendance %, internal marks, fee status, and a
shared notice board. Everything else in your project document (leave, assignments, study
materials, AI assistant) follows the same repository/ViewModel/screen pattern already in this
project — see "Extending it" below.

## What's included

```
SmartStudentPortal/
  app/src/main/java/com/kvcet/smartstudentportal/
    SmartStudentPortalApp.kt        Application class, initializes Firebase
    MainActivity.kt                 Single-activity host for Compose
    data/model/                     Student, AttendanceRecord, MarkRecord, FeeRecord,
                                     NoticeItem, LeaveRequestItem, UserAccount
    data/repository/                AuthRepository, StudentRepository, NoticeRepository
                                     (all Firestore/Auth calls live here)
    ui/auth/                        LoginScreen + AuthViewModel (role-based routing)
    ui/admin/                       AdminDashboardScreen, StudentListScreen,
                                     AddEditStudentScreen, AdminViewModel
    ui/student/                     StudentDashboardScreen, StudentViewModel
    ui/notice/                      NoticeBoardScreen, NoticeViewModel (shared by both roles)
    ui/navigation/                  AppNavHost — single source of truth for screen routing
    ui/theme/                       Color.kt, Theme.kt
  firestore.rules                   Security rules enforcing admin vs student access
```

## 1. Set up Firebase (10 minutes)

1. Go to the [Firebase console](https://console.firebase.google.com) → **Add project**.
2. Inside the project, click **Add app → Android**, and register package name
   `com.kvcet.smartstudentportal`.
3. Download the generated `google-services.json` and place it at
   `app/google-services.json` (same folder as `app/build.gradle.kts`). This repo
   deliberately does not ship a real one — it's tied to your Firebase project.
4. In the console, enable:
   - **Authentication → Sign-in method → Email/Password**
   - **Firestore Database** (start in production mode)
   - **Storage** (for photos/PDFs, used later when you wire up uploads)
5. In Firestore → **Rules**, paste the contents of `firestore.rules` from this project and publish.

## 2. Open and run

1. Open the `SmartStudentPortal` folder in Android Studio (Koala or newer).
2. Let Gradle sync — it will pull Compose, Navigation, and Firebase BOM dependencies.
3. Run on an emulator or device (minSdk 24 / Android 7.0+).

## 3. Create your first accounts

The app has no public sign-up screen by design — the admin controls who gets an account,
matching your project's "admin has complete control" requirement.

**Create the admin account manually** (one-time, via Firebase console):
1. Authentication → Users → Add user → enter your email/password.
2. Firestore → `users` collection → add a document with that user's UID as the document ID:
   ```
   { "uid": "<the UID from step 1>", "email": "admin@kvcet.edu", "role": "admin", "studentId": "" }
   ```

**Create student accounts from inside the app**: once logged in as admin, add students to the
roster via *Manage students*. `AuthRepository.createStudentAccount()` is already written to
create the Firebase Auth login and the linked `users` document in one call — wire a button to it
from `StudentListScreen` when you're ready to issue logins (it's not hooked to a button yet since
the roster is entered before students need to log in, per your prototype scope).

## 4. Seed some test data

For attendance/marks/fees to show anything, add a few documents directly in the Firestore console
(or write a small admin screen later, following the pattern in `AdminViewModel`):

- `attendance`: `{ studentId, date: "2026-07-20", status: "Present" }`
- `marks`: `{ studentId, subject: "Data Structures", examType: "Internal 1", marksScored: 42, maxMarks: 50 }`
- `fees`: `{ studentId, amountDue: 25000, amountPaid: 25000, status: "Paid" }`

`studentId` must match the document ID Firestore assigned when you added the student (visible in
the console under the `students` collection).

## Extending it

Every module follows the same three-layer pattern, so adding the remaining features from your
project doc (assignments, leave requests, study materials, AI assistant) means repeating it:

1. **Model** (`data/model/`) — a plain data class matching the Firestore document shape.
2. **Repository** (`data/repository/`) — CRUD methods using the Firebase SDK, wrapped in
   `Result<T>` for success/failure, following `StudentRepository.kt` as the template.
3. **ViewModel + Screen** (`ui/<feature>/`) — a `StateFlow`-based ViewModel and a Compose screen,
   following `NoticeViewModel`/`NoticeBoardScreen.kt` as the simplest template to copy.
4. **Route** — register the new screen in `AppNavHost.kt`.

`LeaveRequestItem` is already modeled and covered by `firestore.rules`, so leave requests are the
quickest next feature to build if you want a template to follow yourself.

## Notes on choices made for you

- **Firebase over a custom backend**: no server to host, deploy, or pay for — appropriate for a
  student prototype, and it matches "cloud-based" language in your project description.
- **Firestore over Realtime Database**: better querying (`whereEqualTo`, `orderBy`) for
  reports/filters your admin panel will need later.
- **Jetpack Compose over XML layouts**: less boilerplate for a project this size, and it's the
  approach Google now recommends for new apps.
- **AI assistant / AI analytics** from your document aren't implemented yet — they're the most
  open-ended part of the spec (needs an LLM API call and a UI for it) and deserve their own
  focused pass once the core CRUD flows above are working end-to-end.
