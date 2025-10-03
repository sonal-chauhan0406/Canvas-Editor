# 🎨 Canvas Editor

Canvas Editor is a dynamic and intuitive Android application built with **Jetpack Compose** that empowers users to create beautiful, multi-page designs with customizable text. It ensures **secure authentication** and **real-time cloud synchronization** with **Google Firebase**, so your work is always safe and accessible.


## ✨ Features

* **Multi-Page Canvas System** – Create and manage multiple pages within a single project.
* **Interactive Canvas** – Drag, drop, and resize text elements with intuitive touch gestures.
* **Rich Text Styling** – Apply font size, bold, italic, underline, and choose from custom fonts.
* **Undo/Redo State Management** – Easily revert or redo changes for smooth editing.
* **Page Management** – Add, delete, and reorder pages effortlessly.
* **Secure Authentication** – Sign up and log in via **Email/Password** or **Google Sign-In**.
* **Cloud Persistence** – Work is auto-saved to **Cloud Firestore**, linked to your account.

## 🎥 Demo Video

👉 [Watch the walkthrough demo here](#)



## 🛠️ Tech Stack & Architecture

* **UI:** Jetpack Compose
* **Architecture:** MVVM (Model-View-ViewModel)
* **Backend:** Google Firebase
* **Authentication:** Email/Password & Google Sign-In
* **Database:** Cloud Firestore (real-time NoSQL database)
* **Async Tasks:** Kotlin Coroutines
* **Language:** Kotlin


## 🔥 Firebase Setup & Configuration

1. **Create a Firebase Project**

   * Go to the [Firebase Console](https://console.firebase.google.com/).
   * Create a new project.

2. **Register Your Android App**

   * Use the package name: `com.example.celebrare_assignment`.
   * Download the `google-services.json` file.
   * Place it inside the **`app/`** directory.

3. **Enable Authentication Methods**

   * Navigate to **Authentication** in Firebase Console.
   * Enable **Email/Password** and **Google Sign-In**.

4. **Set Up Cloud Firestore**

   * Go to **Firestore Database** in Firebase Console.
   * Create a new database.
   * Start in **Test Mode** (recommended for initial setup).



## 🚀 Getting Started

* Clone the repository.
* Open in **Android Studio** (latest version recommended).
* Add your `google-services.json` file.
* Sync and run the project on an emulator or physical device.



## ✅ Future Enhancements

* Image & Shape insertion.
* Export designs as **PDF/PNG**.
* Collaboration & real-time editing.
* Dark mode support.

