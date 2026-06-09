# Complete XSpoof Build Guide - Windows/Mac/Linux

Since you can't add collaborators, build this yourself! It's easier than you think.

## Prerequisites

### Download & Install:

1. **Android Studio** (includes SDK)
   - Windows/Mac/Linux: https://developer.android.com/studio
   - Install with default settings

2. **Git** (if not installed)
   - https://git-scm.com/downloads

3. **Space Required**: 8GB

## Step 1: Clone Repository

Open terminal/command prompt:

```bash
# Navigate to desired location
cd ~/projects
# or
cd C:\Users\YourName\projects  # Windows

# Clone the repo
git clone https://github.com/a1054800a-boss/xps.git
cd xps

# Switch to magisk-conversion branch
git checkout magisk-conversion
```

## Step 2: Open in Android Studio

1. Open **Android Studio**
2. Click **"Open"** (or File → Open)
3. Select the `xps` folder you just cloned
4. Wait for Gradle sync to complete (1-2 minutes)

## Step 3: Build APK via Android Studio

**Method 1: GUI (Easiest)**

1. Click **Build** menu at top
2. Select **Build Bundle(s) / APK(s)** → **Build APK(s)**
3. Wait 10-15 minutes
4. When done, click **"Locate"** to find the APK

**Method 2: Command Line**

```bash
cd ~/projects/xps

# Build release APK
./gradlew clean assembleRelease
```

**APK will be at:**
```
app/build/outputs/apk/release/app-release.apk
```

## Step 4: Create Magisk Module ZIP

```bash
cd ~/projects/xps/magisk_module

# Windows Command Prompt - use:
cd magisk_module
powershell -Command "Compress-Archive -Path module.prop, common, post-fs-data.sh, service.sh, uninstall.sh, README.md -DestinationPath ../XSpoof-Magisk-v2.0.0.zip"

# Mac/Linux - use:
zip -r ../XSpoof-Magisk-v2.0.0.zip module.prop common/ post-fs-data.sh service.sh uninstall.sh README.md
```

## Step 5: Verify Files

```bash
# Both files should exist:
ls -la ~/projects/xps/app/build/outputs/apk/release/app-release.apk
ls -la ~/projects/xps/XSpoof-Magisk-v2.0.0.zip
```

## Step 6: Upload to GitHub Releases

1. Go to: https://github.com/a1054800a-boss/xps/releases
2. Click **"Create a new release"**
3. **Tag version:** `v2.0.0`
4. **Release title:** `XSpoof v2.0.0 - Magisk Module`
5. **Description:**
```
# XSpoof v2.0.0

## Installation:
1. Install the APK normally
2. Open Magisk Manager → Modules → + → Select the ZIP
3. Reboot

## Files:
- XSpoof-v2.0.0.apk - UI App
- XSpoof-Magisk-v2.0.0.zip - Magisk Module

## Features:
- Device info spoofing
- Android ID spoofing
- Network spoofing
- Per-app targeting
```

6. **Drag and drop files:**
   - `app-release.apk` → rename to `XSpoof-v2.0.0.apk`
   - `XSpoof-Magisk-v2.0.0.zip`

7. Click **"Publish release"**

---

## Troubleshooting

### Build fails: "Android SDK not found"

**Fix:**
1. Open Android Studio
2. Go to **Tools → SDK Manager**
3. Install:
   - Android SDK Platform 33
   - Android SDK Build-Tools 33.x
   - Android Emulator (optional)
4. Retry build

### ZIP creation fails on Windows

Use this instead:
```powershell
cd magisk_module
# Create ZIP manually:
# 1. Select all files (module.prop, common/, etc)
# 2. Right-click → Send to → Compressed (zipped) folder
# 3. Rename to XSpoof-Magisk-v2.0.0.zip
# 4. Move to parent directory
```

### Gradle sync stuck

```bash
# Close Android Studio
# Delete gradle cache
rm -rf ~/.gradle

# Reopen Android Studio - it will re-sync
```

### Out of memory error

Edit `gradle.properties` in project root:
```
org.gradle.jvmargs=-Xmx2048m
```

---

## Quick Command Summary

**Windows (PowerShell):**
```powershell
cd $env:USERPROFILE\projects\xps
git checkout magisk-conversion
.\gradlew clean assembleRelease
cd magisk_module
Compress-Archive -Path module.prop, common, post-fs-data.sh, service.sh, uninstall.sh, README.md -DestinationPath ../XSpoof-Magisk-v2.0.0.zip
cd ..
# Then upload to GitHub
```

**Mac/Linux:**
```bash
cd ~/projects/xps
git checkout magisk-conversion
./gradlew clean assembleRelease
cd magisk_module
zip -r ../XSpoof-Magisk-v2.0.0.zip module.prop common/ post-fs-data.sh service.sh uninstall.sh README.md
cd ..
# Then upload to GitHub
```

---

## After Release

Once files are uploaded:

1. **Share the release link:** `https://github.com/a1054800a-boss/xps/releases/tag/v2.0.0`
2. **Users can download** both files directly from GitHub
3. **Install on Android** as described above

---

## Need Help?

If build fails:
1. Post error message
2. I'll help troubleshoot
3. We'll fix it together

You've got this! 🚀
