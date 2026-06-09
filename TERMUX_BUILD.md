# XSpoof Build Guide for Termux (Android)

This guide explains how to build XSpoof APK and Magisk module ZIP using **Termux** on your Android device.

## Prerequisites

### Apps to Install
1. **Termux** - Terminal emulator for Android
2. **Magisk/LSPosed** - For module testing (optional)

### Get Termux
- Download from: https://f-droid.org/en/packages/com.termux/
- Or: https://github.com/termux/termux-app/releases

## Step 1: Install Required Tools in Termux

Open Termux and run these commands:

```bash
# Update package manager
pkg update && pkg upgrade

# Install essential tools
pkg install -y git openjdk-17 gradle wget curl

# Verify installations
java -version
gradle -version
git --version
```

**Expected output:**
```
openjdk version "17.0.x" ...
Gradle x.x ...
git version ...
```

## Step 2: Clone the Repository

```bash
# Create projects directory
mkdir -p ~/projects
cd ~/projects

# Clone the repository
git clone https://github.com/a1054800a-boss/xps.git
cd xps

# Switch to magisk-conversion branch
git checkout magisk-conversion
```

## Step 3: Configure Android SDK

```bash
# Install Android SDK tools
pkg install -y android-tools

# Set ANDROID_SDK_ROOT (add to ~/.bashrc for persistence)
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT

# Add to bashrc for future sessions
echo "export ANDROID_SDK_ROOT=\$PREFIX/opt/android-sdk" >> ~/.bashrc
echo "export ANDROID_HOME=\$ANDROID_SDK_ROOT" >> ~/.bashrc
source ~/.bashrc
```

## Step 4: Set Gradle Properties

Create `~/.gradle/gradle.properties`:

```bash
mkdir -p ~/.gradle
cat > ~/.gradle/gradle.properties << 'EOF'
org.gradle.jvmargs=-Xmx1024m
org.gradle.parallel=true
org.gradle.daemon=false
android.useAndroidX=true
android.enableJetifier=true
EOF
```

## Step 5: Build the APK

```bash
cd ~/projects/xps

# Make build scripts executable
chmod +x ./gradlew

# Clean build
./gradlew clean

# Build release APK (this takes ~5-10 minutes)
./gradlew assembleRelease -x lint
```

**Check progress in output:**
```
> Task :app:packageReleaseResources
> Task :app:compileReleaseKotlin
> Task :app:dexReleaseClasses
> Task :app:packageRelease
> Task :app:signReleaseApk
```

### APK Location
```
~/projects/xps/app/build/outputs/apk/release/app-release.apk
```

**File size:** ~5-8 MB

## Step 6: Create Magisk Module ZIP

```bash
cd ~/projects/xps

# Navigate to magisk module directory
cd magisk_module

# Create the ZIP file
zip -r ../XSpoof-Magisk-v2.0.0.zip \
    module.prop \
    common/system.prop \
    post-fs-data.sh \
    service.sh \
    uninstall.sh \
    README.md

# Verify ZIP was created
cd ..
ls -lah XSpoof-Magisk-v2.0.0.zip
```

**Expected output:**
```
-rw-r--r-- 1 user group 12K Jun 9 20:50 XSpoof-Magisk-v2.0.0.zip
```

## Step 7: Copy Files for Installation

### Option A: Using File Manager

```bash
# Copy to Downloads folder (accessible via file manager)
cp ~/projects/xps/app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp ~/projects/xps/XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

# Verify
ls ~/storage/downloads/ | grep XSpoof
```

### Option B: Direct Installation via ADB (if connected to PC)

```bash
# If you have adb available in Termux
adb devices

# Install APK
adb install ~/projects/xps/app/build/outputs/apk/release/app-release.apk

# Or manually transfer files via USB
```

## Step 8: Install on Your Device

### Install the APK

**Method 1: Using File Manager**
1. Open File Manager
2. Navigate to `/sdcard/Download/` or `/storage/emulated/0/Download/`
3. Find `XSpoof-v2.0.0.apk`
4. Tap to install
5. Allow installation from unknown sources if prompted

**Method 2: Using Termux**
```bash
# If termux-open is available
termux-open ~/storage/downloads/XSpoof-v2.0.0.apk
```

### Install the Magisk Module

1. **Open Magisk Manager**
2. **Tap "Modules"**
3. **Tap the "+" button** (or "Install from Storage")
4. **Select** `XSpoof-Magisk-v2.0.0.zip`
5. **Wait for installation** to complete
6. **Reboot device**

## Step 9: Verify Installation

### Check if APK is installed
```bash
# List installed packages
pm list packages | grep xspoof

# Expected output:
# com.example.xspoof
```

### Check Xposed logs
```bash
# View recent logs
logcat | grep XSpoof

# Or save to file
logcat > ~/xspoof_logs.txt
```

### Open the App
```bash
# Launch XSpoof UI
am start -n com.example.xspoof/.ui.MainActivity
```

## Troubleshooting

### Build Fails with "SDK not found"

```bash
# Manually set SDK
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT

# Try again
./gradlew assembleRelease -x lint
```

### Out of Memory Error

```bash
# Increase heap size
export GRADLE_OPTS="-Xmx512m"
./gradlew assembleRelease -x lint
```

### Gradle Daemon Issues

```bash
# Kill gradle daemon
./gradlew --stop

# Clean and rebuild
./gradlew clean assembleRelease
```

### APK Installation Fails

```bash
# Verify APK signature
jarsigner -verify app/build/outputs/apk/release/app-release.apk

# Check file integrity
ls -lah app/build/outputs/apk/release/app-release.apk
```

### Module ZIP Not Installing

```bash
# Verify ZIP structure
unzip -l XSpoof-Magisk-v2.0.0.zip

# Should show:
# module.prop
# common/system.prop
# post-fs-data.sh
# service.sh
# etc.
```

## Quick Build Script

Save this as `~/build-xspoof.sh`:

```bash
#!/bin/bash
set -e

echo "🔨 Building XSpoof..."

cd ~/projects/xps

# Update code
echo "📡 Pulling latest changes..."
git pull origin magisk-conversion

# Build APK
echo "📦 Building APK..."
./gradlew clean assembleRelease -x lint

# Create Magisk ZIP
echo "📦 Creating Magisk module ZIP..."
cd magisk_module
zip -r ../XSpoof-Magisk-v2.0.0.zip \
    module.prop \
    common/system.prop \
    post-fs-data.sh \
    service.sh \
    uninstall.sh \
    README.md
cd ..

# Copy to Downloads
echo "📱 Copying to Downloads..."
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

echo "✅ Build complete!"
echo "📁 APK: ~/storage/downloads/XSpoof-v2.0.0.apk"
echo "📁 Module: ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip"
```

Make it executable:
```bash
chmod +x ~/build-xspoof.sh
```

Run it:
```bash
~/build-xspoof.sh
```

## File Locations Summary

```
Termux Home:              ~/
Projects:                 ~/projects/xps/
Source Code:              ~/projects/xps/app/src/main/
Built APK:                ~/projects/xps/app/build/outputs/apk/release/app-release.apk
Magisk ZIP:               ~/projects/xps/XSpoof-Magisk-v2.0.0.zip
Downloaded (for install): ~/storage/downloads/XSpoof-*.apk
                          ~/storage/downloads/XSpoof-*.zip
Downloads on device:      /sdcard/Download/
```

## Environment Variables Cheat Sheet

```bash
# Add these to ~/.bashrc for persistent setup
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
export PATH=$PATH:$ANDROID_SDK_ROOT/tools:$ANDROID_SDK_ROOT/platform-tools
export GRADLE_OPTS="-Xmx512m"
export JAVA_HOME=$PREFIX/opt/openjdk
```

## Performance Tips

- **Build on charger** - Building drains battery
- **Use WiFi** - Downloads are faster
- **Close other apps** - Frees up RAM for build process
- **Build at night** - Keeps device cool

## Updating the Source Code

When you want to rebuild after updates:

```bash
cd ~/projects/xps
git pull origin magisk-conversion
./gradlew clean assembleRelease -x lint
```

## Getting Help

If build fails:
1. **Check logs:** `cat build.log`
2. **Clean everything:** `./gradlew clean`
3. **Update tools:** `pkg upgrade`
4. **Check disk space:** `df -h`
5. **Check RAM:** `free -h`

## Alternative: Use Pre-built APK

If building is too slow or fails, you can:

1. **Download pre-built APK** from GitHub Releases
2. **Just create the Magisk ZIP** (it's quick)
3. **Install both files**

Let me know if you need help with any step!
