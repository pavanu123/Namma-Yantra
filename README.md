# 🚜 Namma-Yantra

**Peer-to-Peer Agricultural Machinery Rental Platform**

-->Namma-Yantra is an Android application that connects farmers and agricultural equipment owners through a peer-to-peer rental platform. Small farmers can rent tractors, power tillers, harvesters, and other machinery from nearby owners instead of purchasing expensive equipment.

---

## 📌 Problem Statement

Many small and marginal farmers cannot afford expensive farming machinery. At the same time:
- Large farmers keep equipment idle for long periods
- Rental processes are handled manually
- Middlemen charge high commissions
- There is no transparent pricing system

**Namma-Yantra solves this problem using:**
- Android Application
- Firebase Realtime Database
- Direct farmer-owner connection
- No middlemen

---

## ✨ Features

### For Farmers
- 🔍 Browse available equipment near you
- 📅 View equipment details with hourly/daily rates
- 📨 Send rental requests to equipment owners
- 📋 Track request status (pending/accepted/declined)
- 🔎 Search equipment by name or location

### For Equipment Owners
- ➕ Add new equipment with rates and location
- ✏️ Edit or delete your equipment listings
- 📝 View incoming rental requests from farmers
- ✅ Accept or decline rental requests
- 📊 Monitor your equipment rental status

### Common Features
- 👤 User registration and login (Email/Password)
- 🎭 Role-based dashboard (Farmer/Owner)
- 🔐 Firebase Authentication
- ☁️ Real-time data sync with Firebase
- 📱 Material Design 3 UI

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Android development language |
| Jetpack Compose | Modern UI toolkit |
| Firebase Authentication | User login/signup |
| Firebase Realtime Database | Data storage and sync |
| MVVM Architecture | Clean architecture pattern |
| Material Design 3 | UI components and styling |
| Coroutines | Async operations |

---

## 📁 Project Structure
Namma-Yantra/
├── app/
│ ├── src/main/java/com/nammayantra/
│ │ ├── MainActivity.kt # Main entry point
│ │ ├── models/ # Data classes (User, Equipment, Request)
│ │ ├── viewmodels/ # MVVM ViewModels
│ │ ├── repositories/ # Firebase data operations
│ │ └── ui/theme/ # Compose theming
│ └── build.gradle.kts # Dependencies
├── gradle/
└── README.md

text

---

## 🚀 Installation & Setup

### Prerequisites
- Android Studio (Latest version)
- Android SDK API 24+
- Java 11 or higher
- Internet connection

### Steps to Run Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/pavanu123/Namma-Yantra.git
   cd Namma-Yantra
Open in Android Studio

Open Android Studio

Select "Open an Existing Project"

Choose the cloned NammaYantra folder

Sync Gradle

Click "Sync Now" when prompted

Wait for dependencies to download

Add Firebase Configuration

Download google-services.json from Firebase Console

Place it in the app/ folder

Build and Run

Connect an Android device or start an emulator

Click the Run button (green triangle)

📱 How to Use
Register as Farmer
Open the app → Click "REGISTER HERE"

Fill your details (name, email, phone, village, district)

Select "Farmer" as role

Click "REGISTER"

Register as Owner
Follow same steps but select "Equipment Owner"

After login, click "Add New Equipment"

Fill equipment details (name, type, rates, location)

Click "ADD EQUIPMENT"

Send Rental Request (Farmer)
Login as Farmer → Click "Browse Available Equipment"

Select any equipment → Click "Request Rent"

Owner will receive your request

Manage Requests (Owner)
Login as Owner → Click "Rental Requests"

View pending requests → Accept or Decline

📸 Screenshots
Login Screen	Farmer Dashboard	Browse Equipment
(Add screenshot)	(Add screenshot)	(Add screenshot)
Add Equipment	Rental Requests	My Requests
(Add screenshot)	(Add screenshot)	(Add screenshot)
🔗 Demo / Live Links
GitHub Repository: https://github.com/pavanu123/Namma-Yantra

APK Download: Download Namma-Yantra APK

📊 Future Enhancements
Version 2.0
Push Notifications

UPI Payments Integration

Multilingual Support (Kannada, Hindi)

Ratings & Reviews System

Version 3.0
AI-based Pricing Suggestions

In-App Chat between farmers and owners

Version 4.0
IoT Equipment Tracking

Insurance Module

Web Portal for Management

👨‍💻 Developer
Name:	Pavan U
USN	:1KG22CS082
College:	KS School of Engineering and Management
Email	:pavanumesh221@gmail.com

🙏 Acknowledgments
Team MindMatrix for project evaluation guidance

Firebase for backend services

JetBrains for Kotlin and Android Studio

📝 License
This project is for educational purposes as part of the internship program at MindMatrix.

Built with ❤️ for Indian farmers
