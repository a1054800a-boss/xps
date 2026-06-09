# XSpoof - Building in Termux

This guide explains how to build and install XSpoof using **Termux** on Android.

## Prerequisites

### 1. Install Termux

- Download from [F-Droid](https://f-droid.org/en/packages/com.termux/) (recommended)
- Or from [Google Play Store](https://play.google.com/store/apps/details?id=com.termux)

### 2. Install Required Tools

```bash
# Update package lists
pkg update && pkg upgrade

# Install essential tools
pkg install git
pkg install gradle
pkg install android-tools  # for adb
pkg install android-sdk    # optional, for SDK access
```

### 3. Clone Your Repository

```bash
cd $HOME
git clone https://github.com/e646776444-sys/123.git
cd 123
```

## Building

### Option 1: Automated Build Script (Recommended)

```bash
bash scripts/build-termux.sh
```

This will:
1. Create Magisk module ZIP (`xspoof-magisk.zip`)
2. Build Android APK (`app/build/outputs/apk/release/app-release.apk`)
3. Display installation instructions

### Option 2: Manual Build

#### Build Magisk Module

```bash
cd magisk_module
zip -r ../xspoof-magisk.zip .
cd ..
```

#### Build APK

```bash
./gradlew build
```

Output: `app/build/outputs/apk/release/app-release.apk`

## Installation

### Using Installation Script (Recommended)

```bash
bash scripts/install-termux.sh
```

Requires:
- APK already built
- Device connected via USB with ADB enabled
- USB Debugging enabled on device

### Manual Installation

#### Install APK

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

#### Install Magisk Module

```bash
# Push module to device
adb push xspoof-magisk.zip /sdcard/Download/

# Then on device:
# 1. Open Magisk Manager
# 2. Tap Modules
# 3. Tap Install from storage
# 4. Select xspoof-magisk.zip
# 5. Reboot
```

Or copy directly:

```bash
adb shell mkdir -p /data/adb/modules/xspoof
adb push magisk_module/* /data/adb/modules/xspoof/
adb shell chmod +x /data/adb/modules/xspoof/post-fs-data.sh
adb shell chmod +x /data/adb/modules/xspoof/service.sh
```

## Verification

After installation and reboot:

```bash
# Check if module is installed
adb shell ls -la /data/adb/modules/xspoof/

# View module log
adb shell cat /data/adb/modules/xspoof/xspoof.log

# Check applied properties
adb shell getprop ro.product.manufacturer
adb shell getprop ro.product.model

# View config file
adb shell cat /data/adb/modules/xspoof/config.json
```

## Troubleshooting

### ADB Connection Issues

```bash
# List connected devices
adb devices

# If device not shown:
# 1. Enable USB Debugging in Developer Options
# 2. Check USB connection
# 3. Restart adb server
adb kill-server
adb start-server
adb devices
```

### Build Failures

**Gradle not found:**
```bash
pkg install gradle
```

**SDK not found:**
```bash
pkg install android-sdk
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
```

**Permission denied on scripts:**
```bash
chmod +x scripts/build-termux.sh
chmod +x scripts/install-termux.sh
```

### Module Not Working

1. **Check installation:**
   ```bash
   adb shell ls /data/adb/modules/xspoof/
   ```

2. **Check permissions:**
   ```bash
   adb shell chmod +x /data/adb/modules/xspoof/post-fs-data.sh
   adb shell chmod +x /data/adb/modules/xspoof/service.sh
   adb shell chmod 666 /data/adb/modules/xspoof/config.json
   ```

3. **View logs:**
   ```bash
   adb shell tail -f /data/adb/modules/xspoof/xspoof.log
   ```

4. **Verify properties:**
   ```bash
   adb shell getprop | grep ro.product
   adb shell getprop | grep ro.build
   ```

## Environment Variables

Add to `~/.bashrc` for convenience:

```bash
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$PREFIX/opt/android-sdk
```

Then reload:
```bash
source ~/.bashrc
```

## Build Configuration

To customize the build, edit:

- **APK package name**: `app/build.gradle` → `applicationId`
- **Module ID**: `magisk_module/module.prop` → `id`
- **Minimum Android version**: `app/build.gradle` → `minSdk`
- **Target Android version**: `app/build.gradle` → `targetSdk`

## Advanced: Building without Android SDK

If you only want to build the Magisk module (no APK):

```bash
cd magisk_module
zip -r ../xspoof-magisk.zip .
cd ..

# Then push to device
adb push xspoof-magisk.zip /sdcard/Download/
```

## Notes

- **First build takes time** (downloads dependencies, ~10-15 minutes)
- **Requires ~500MB disk space** for Gradle, SDK, and build artifacts
- **Reboot required** after installing Magisk module for changes to take effect
- **Device must be rooted** with Magisk installed
- **USB Debugging** must be enabled on device

## Getting Help

- [Termux Wiki](https://wiki.termux.com/)
- [Gradle Documentation](https://gradle.org/docs/)
- [Magisk Documentation](https://topjohnwu.github.io/Magisk/)
- [Android Build Documentation](https://developer.android.com/build)
