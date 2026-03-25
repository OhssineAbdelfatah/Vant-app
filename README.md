# Vant

**Vant** is an Android media-tracker app built with Kotlin, Jetpack Compose, Room, and the TMDB API.  
It lets you search for movies & TV shows and keep a local watch-list on your device.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Clone the Repository](#clone-the-repository)
3. [Build the APK](#build-the-apk)
   - [Option A – Android Studio (recommended for development)](#option-a--android-studio-recommended-for-development)
   - [Option B – Command line with Gradle](#option-b--command-line-with-gradle)
   - [Option C – Docker (no local SDK needed)](#option-c--docker-no-local-sdk-needed)
4. [Install the APK on your device](#install-the-apk-on-your-device)
5. [Signing notes](#signing-notes)

---

## Prerequisites

| Tool | Minimum version | Notes |
|------|-----------------|-------|
| Git | any modern version | For cloning |
| JDK | 17 | Required by AGP 9.x. Use [Eclipse Temurin](https://adoptium.net/) or any JDK 17 distribution |
| Android Studio | Meerkat (2024.3+) | Bundles the Android SDK and Gradle; **skip if using Docker** |
| **— or —** Docker & Docker Compose | 24 + | Alternative build path; no local SDK needed |

> **Minimum Android version:** API 24 (Android 7.0 Nougat)

---

## Clone the Repository

```bash
git clone https://github.com/OhssineAbdelfatah/Vant-app.git
cd Vant-app/Vant
```

All build commands below are run from the `Vant/` directory (the directory that contains `gradlew`).

---

## Build the APK

### Option A – Android Studio (recommended for development)

1. Open **Android Studio** → **File → Open** → select the `Vant-app/Vant` folder.
2. Wait for Gradle sync to finish.
3. In the menu bar choose **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
4. Android Studio will notify you when the build is complete and show a **locate** link.  
   The APK is written to:
   ```
   app/build/outputs/apk/release/app-release.apk   # release
   app/build/outputs/apk/debug/app-debug.apk        # debug
   ```

### Option B – Command line with Gradle

Make sure `JAVA_HOME` points to a JDK 17 installation and `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) points to your Android SDK.

```bash
# macOS / Linux
./gradlew assembleRelease

# Windows
gradlew.bat assembleRelease
```

The signed release APK will be at:
```
app/build/outputs/apk/release/app-release.apk
```

To build an unsigned debug APK instead:
```bash
./gradlew assembleDebug
```

### Option C – Docker (no local SDK needed)

Docker Compose pulls a pre-built Android SDK image and compiles the project inside a container – no JDK or Android SDK installation required on your machine.

```bash
# From the Vant/ directory
docker compose run --rm android-builder
```

The APK is written to the same path on your **host** machine (the project directory is bind-mounted):
```
app/build/outputs/apk/release/app-release.apk
```

---

## Install the APK on your device

### Via ADB (USB cable)

1. Enable **Developer options** on your Android phone:  
   *Settings → About phone → tap "Build number" 7 times.*
2. Enable **USB debugging** inside Developer options.
3. Connect your phone to your computer with a USB cable.
4. Install the APK:

```bash
adb install app/build/outputs/apk/release/app-release.apk
```

### By copying the file directly

1. Copy `app-release.apk` to your phone (via USB file transfer, cloud storage, email, etc.).
2. On the phone, open the APK file with a file manager.
3. If prompted, allow **Install unknown apps** for that file manager in Settings.
4. Tap **Install** and follow the on-screen instructions.

---

## Signing notes

> ⚠️ **Security warning:** The keystore bundled in this repository (`keystore/vant-release.jks`) and its credentials are **public and must never be used for production releases**.  
> Anyone with access to this repo can sign APKs with the same identity, which means your app could be impersonated or tampered with.  
> Always generate your own private keystore for any app you ship publicly.

The default credentials are referenced in `app/build.gradle.kts` and can be overridden with environment variables before building:

| Environment variable | Default value | Description |
|----------------------|---------------|-------------|
| `KEYSTORE_PATH` | `keystore/vant-release.jks` | Path to the `.jks` file |
| `KEYSTORE_PASS` | `vant2026!` | Keystore password |
| `KEY_ALIAS` | `vant` | Key alias inside the keystore |
| `KEY_PASS` | `vant2026!` | Key password |

For a production release, replace the keystore with your own and supply the credentials as environment variables (or via CI secrets) rather than using the defaults.
