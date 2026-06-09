# Pre-Built XSpoof Files for Termux Users

Since building from source on Termux requires the full Android SDK (which is complex to set up on mobile), here are **pre-built files ready to install**.

## Download & Install (Easiest Method)

### Files Ready:
1. **XSpoof-v2.0.0.apk** - UI Application
2. **XSpoof-Magisk-v2.0.0.zip** - Magisk Module

These are on the `magisk-conversion` branch.

### Installation Steps:

**Step 1: Download APK**
- Go to: https://github.com/a1054800a-boss/xps/releases (or download from branch)
- Download: `XSpoof-v2.0.0.apk`

**Step 2: Install APK**
```bash
# If using Termux to install
adb install ~/storage/downloads/XSpoof-v2.0.0.apk

# Or manually: Open file manager → Downloads → tap APK → Install
```

**Step 3: Install Magisk Module**
1. Open **Magisk Manager**
2. Tap **Modules** tab
3. Tap **+** button
4. Select **XSpoof-Magisk-v2.0.0.zip**
5. Wait for "Installation Successful"
6. **Reboot device**

**Step 4: Done!**
- Open XSpoof app from app drawer
- Configure settings
- All spoofing will be active

---

## Building from Source (Advanced)

If you still want to build on Termux, you need:

```bash
# Install full Android SDK
pkg install -y android-sdk

# Set paths
export ANDROID_HOME=$PREFIX/opt/android-sdk
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export JAVA_HOME=/data/data/com.termux/files/usr

# Build
cd ~/projects/xps
./gradlew clean assembleRelease -x lint --no-daemon
```

**This requires 3GB+ space and 1GB+ RAM.**

---

## Why Building on Termux is Hard

The `com.android.application` plugin requires:
- Android SDK Platform Tools
- Android SDK Build Tools
- Multiple SDK libraries

These take up significant storage on mobile devices.

## Recommended: Use Pre-built Files

The easiest way is to:
1. Download the pre-built APK
2. Download the pre-built Magisk ZIP
3. Install both on your device
4. Done!

No build process needed! 🎉
