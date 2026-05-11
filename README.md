# LocalLens

LocalLens is an Android application for discovering local street food stalls and helping stall owners manage their own stall listings. The app is focused on discovery and stall profile management only.

It does not include food ordering, delivery, online payment, live vendor tracking, sales dashboards, or chat.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Storage Paths](#storage-paths)
- [Build and Run](#build-and-run)

## Overview

LocalLens supports two roles:

- Customer
- Stall Owner

The customer side is used to browse local stalls, view map/list information, open stall details, and save favorite places.

The stall owner side is used to register/login, add stalls, manage multiple owned stalls, edit stall information, manage menu items, upload photos, manage opening hours, view reviews by stall, and update the owner profile.

## Features

### Authentication

- Username and password registration
- Username and password login
- Role selection during registration
- Role-based navigation after login
- Customer users open the customer app
- Stall owner users open the stall owner dashboard

Important: this project currently uses a custom username/password flow stored in Firestore. It does not use Firebase Authentication.

### Customer Side

Current customer screens:

- Home
- Map
- Stall detail
- Favorites
- Profile

The customer UI is available as the discovery side of the app. Some customer data is still local/demo data while the Firebase integration is being expanded.

### Stall Owner Side

Owner bottom navigation:

- Dashboard
- My Stall
- Reviews
- Profile

Current owner features:

- Dashboard summary
- Add multiple stalls per owner
- Switch between owned stalls
- Add and edit stall information
- Manage menu items
- Toggle menu item availability
- Delete menu items
- Manage opening hours
- Upload cover photo
- Upload additional stall photos
- Store uploaded image URLs in Firestore
- View reviews filtered by selected stall
- Disable a stall by setting status to `inactive`
- Edit owner profile
- Logout

## Tech Stack

- Kotlin
- Android
- Jetpack Compose
- Material 3
- Gradle Kotlin DSL
- Firebase Firestore
- Firebase Storage
- Google Services Gradle plugin
- Google Maps SDK
- Maps Compose

Android configuration:

- Package name: `org.classapp.locallens`
- Minimum SDK: 24
- Target SDK: 36
- Compile SDK: 36
- Java target: 11

## Project Structure

```text
app/src/main/java/org/classapp/locallens/
  MainActivity.kt
  data/
    FirestoreRepository.kt
    UserFakeData.kt
  model/
    AppUser.kt
    MenuItem.kt
    ReviewItem.kt
    StallProfile.kt
    UserStall.kt
  ui/
    LocalLensApp.kt
    auth/
      AuthScreen.kt
    customer/
      CustomerHomeScreen.kt
    owner/
      StoreOwnerApp.kt
      MyStallMode.kt
      OwnerTab.kt
      components/
      screens/
    user/
      UserApp.kt
      HomeScreen.kt
      MapScreen.kt
      DetailScreen.kt
      FavoriteScreen.kt
      ProfileScreen.kt
    theme/
      Color.kt
      Theme.kt
      Type.kt
```

## Storage Paths

Current Firebase Storage paths:

```text
stall_photos/{ownerId}/{stallId}/{fileName}
review_photos/{userId}/{fileName}
```

The stall owner side currently supports:

- Uploading a cover photo
- Uploading additional stall photos
- Saving the uploaded image URLs to Firestore

## Build and Run

### Requirements

- Android Studio
- Android SDK
- JDK 11 or newer
- Firebase project configured
- `app/google-services.json`

### Build

From the project root:

```powershell
.\gradlew.bat assembleDebug
```

### Run

1. Open the project in Android Studio.
2. Sync Gradle.
3. Select an emulator or physical Android device.
4. Run the `app` configuration.
