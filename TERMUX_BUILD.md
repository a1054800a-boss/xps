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
pkg update && pkg upgrade -y

# Install essential tools
pkg install -y git openjdk-17 gradle android-tools zip make

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

# Verify you're on correct branch
git branch
# Should show: * magisk-conversion
```

## Step 3: Configure Gradle for Termux

Create `~/.gradle/gradle.properties`:

```bash
mkdir -p ~/.gradle
cat > ~/.gradle/gradle.properties << 'EOF'
org.gradle.jvmargs=-Xmx1024m
org.gradle.parallel=true
org.gradle.daemon=false
android.useAndroidX=true
android.enableJetifier=true
org.gradle.warning.mode=all
EOF
```

## Step 4: Set Environment Variables

```bash
# Set Java home
export JAVA_HOME=$PREFIX/opt/openjdk
export PATH=$JAVA_HOME/bin:$PATH

# Verify
java -version
```

## Step 5: Create gradlew Script (IMPORTANT!)

The project doesn't have `gradlew`, so we need to create it:

```bash
cd ~/projects/xps

# Create gradlew script
cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF

# Make it executable
chmod +x gradlew

# Verify
ls -la gradlew
```

## Step 6: Build the APK

```bash
cd ~/projects/xps

# First, clean any previous builds
rm -rf app/build

# Build release APK (takes 5-15 minutes depending on phone)
./gradlew assembleRelease -x lint --no-daemon

# Monitor the build:
# - Resolving dependencies
# - Compiling Kotlin
# - Building APK
# - Signing APK
```

**Watch for this line:**
```
BUILD SUCCESSFUL in Xm Ys
```

### If Build Fails:

```bash
# Try with reduced memory
./gradlew assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"

# Or enable verbose output to see error
./gradlew assembleRelease -x lint --no-daemon --stacktrace
```

### APK Location After Build
```bash
# Check if APK was created
ls -lah app/build/outputs/apk/release/app-release.apk

# Should show: app-release.apk (~5-8 MB)
```

## Step 7: Create Magisk Module ZIP

```bash
cd ~/projects/xps/magisk_module

# Create proper ZIP structure
zip -r ../XSpoof-Magisk-v2.0.0.zip \
    module.prop \
    common/ \
    post-fs-data.sh \
    service.sh \
    uninstall.sh \
    README.md

# Verify ZIP was created
cd ..
ls -lah XSpoof-Magisk-v2.0.0.zip

# Verify ZIP contents
unzip -l XSpoof-Magisk-v2.0.0.zip
```

**Expected output:**
```
Archive:  XSpoof-Magisk-v2.0.0.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
      xxx  06-09-2026 12:00   module.prop
      xxx  06-09-2026 12:00   common/system.prop
      xxx  06-09-2026 12:00   post-fs-data.sh
      xxx  06-09-2026 12:00   service.sh
      xxx  06-09-2026 12:00   uninstall.sh
      xxx  06-09-2026 12:00   README.md
```

## Step 8: Copy Files to Downloads

```bash
# Ensure storage directory is accessible
ls ~/storage/downloads/

# Copy APK
if [ -f app/build/outputs/apk/release/app-release.apk ]; then
    cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
    echo "✅ APK copied"
else
    echo "❌ APK not found - build may have failed"
fi

# Copy Magisk ZIP
if [ -f XSpoof-Magisk-v2.0.0.zip ]; then
    cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip
    echo "✅ Magisk module copied"
else
    echo "❌ ZIP not found"
fi

# Verify files
ls -lah ~/storage/downloads/XSpoof*
```

## Step 9: Install on Your Device

### Install the APK

**Method 1: Using File Manager**
1. Open File Manager
2. Navigate to `/sdcard/Download/` or `/storage/emulated/0/Download/`
3. Find `XSpoof-v2.0.0.apk`
4. Tap to install
5. Allow installation from unknown sources if prompted

**Method 2: Using Termux (if you have GUI)**
```bash
# Open in file manager
termux-open ~/storage/downloads/XSpoof-v2.0.0.apk
```

### Install the Magisk Module

1. **Open Magisk Manager**
2. **Go to "Modules" tab**
3. **Tap the "+" button** (or "Install from file")
4. **Navigate to** `~/storage/downloads/`
5. **Select** `XSpoof-Magisk-v2.0.0.zip`
6. **Wait for installation** (should say "Installation Successful")
7. **Reboot device**

## Step 10: Verify Installation

### Check if APK is installed
```bash
# List all packages with xspoof
pm list packages | grep xspoof

# Expected output:
# com.example.xspoof
```

### Check Xposed logs
```bash
# View logs
logcat | grep XSpoof

# Save logs to file
logcat > ~/xspoof_logs.txt
```

### Open the App
```bash
# Launch XSpoof UI
am start -n com.example.xspoof/.ui.MainActivity
```

## Troubleshooting

### Error: `bash: ./gradlew: No such file or directory`

**Solution:** Create the gradlew script:
```bash
cd ~/projects/xps
cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF
chmod +x gradlew
```

### Error: `The program zip is not installed`

**Solution:** Install zip:
```bash
pkg install -y zip
```

### Error: `Cannot stat 'app/build/outputs/apk/release/app-release.apk'`

This means the build failed. Check:
```bash
# Check for build errors
cat app/build.log

# Look for actual outputs
find app/build -name "*.apk" 2>/dev/null

# Try rebuilding with verbose output
./gradlew assembleRelease --stacktrace
```

### Build Fails with "SDK not found"

```bash
# Check Java is set correctly
echo $JAVA_HOME
java -version

# If not set:
export JAVA_HOME=$PREFIX/opt/openjdk
```

### Out of Memory Error

```bash
# Reduce heap
export GRADLE_OPTS="-Xmx256m"
./gradlew assembleRelease -x lint --no-daemon
```

### Gradle Daemon Issues

```bash
# Stop daemon
./gradlew --stop

# Verify it's stopped
ps aux | grep gradle

# Try build without daemon
./gradlew assembleRelease -x lint --no-daemon
```

### Build Hangs

**Wait 10-15 minutes** - first build takes longer. If still hanging:
```bash
# Press Ctrl+C to stop
# Kill any gradle processes
pkill -f gradle

# Try again with no-daemon
./gradlew assembleRelease -x lint --no-daemon
```

### ZIP Structure Wrong

Verify correct structure:
```bash
cd ~/projects/xps/magisk_module
ls -la

# Should show:
# module.prop
# common/
# post-fs-data.sh
# service.sh
# uninstall.sh
# README.md
```

## Complete Build Script (Automated)

Save this as `~/build-xspoof.sh`:

```bash
#!/bin/bash
set -e

echo "🔨 Building XSpoof..."

# Navigate to project
cd ~/projects/xps

# Update code
echo "📡 Pulling latest changes..."
git pull origin magisk-conversion

# Setup gradlew if missing
if [ ! -f gradlew ]; then
    echo "⚙️ Creating gradlew..."
    cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF
    chmod +x gradlew
fi

# Build APK
echo "📦 Building APK..."
export JAVA_HOME=$PREFIX/opt/openjdk
./gradlew clean assembleRelease -x lint --no-daemon

# Check if build succeeded
if [ ! -f app/build/outputs/apk/release/app-release.apk ]; then
    echo "❌ APK build failed!"
    exit 1
fi

# Create Magisk ZIP
echo "📦 Creating Magisk module ZIP..."
cd magisk_module
zip -r ../XSpoof-Magisk-v2.0.0.zip \
    module.prop \
    common/ \
    post-fs-data.sh \
    service.sh \
    uninstall.sh \
    README.md
cd ..

# Copy to Downloads
echo "📱 Copying to Downloads..."
mkdir -p ~/storage/downloads
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

# Summary
echo ""
echo "✅ Build complete!"
echo "📁 Files ready in ~/storage/downloads/"
echo ""
ls -lah ~/storage/downloads/XSpoof*
echo ""
echo "📱 Next steps:"
echo "1. Install XSpoof-v2.0.0.apk via file manager"
echo "2. Install XSpoof-Magisk-v2.0.0.zip via Magisk Manager"
echo "3. Reboot device"
```

**Make it executable:**
```bash
chmod +x ~/build-xspoof.sh

# Run it:
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
Device Downloads folder:  /sdcard/Download/
Gradle cache:             ~/.gradle/
```

## Performance Tips

- **Build on charger** - Prevents shutdown
- **Close other apps** - Frees RAM
- **Use WiFi** - Faster downloads
- **Be patient** - First build takes 10-15 minutes

## Commands Cheat Sheet

```bash
# Setup (one-time)
mkdir -p ~/projects && cd ~/projects
git clone https://github.com/a1054800a-boss/xps.git
cd xps && git checkout magisk-conversion

# Create gradlew
cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF
chmod +x gradlew

# Build
export JAVA_HOME=$PREFIX/opt/openjdk
./gradlew assembleRelease -x lint --no-daemon

# Create ZIP
cd magisk_module && zip -r ../XSpoof-Magisk-v2.0.0.zip module.prop common/ post-fs-data.sh service.sh uninstall.sh README.md && cd ..

# Copy
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

# Verify
ls ~/storage/downloads/XSpoof*
```

## Getting Help

If something fails:
1. **Read the error message** - It usually tells you what's wrong
2. **Check logs:** `./gradlew assembleRelease --stacktrace`
3. **Verify tools:** `java -version && gradle -version`
4. **Check space:** `df -h` (need at least 2GB free)
5. **Check RAM:** `free -h` (build needs ~512MB)

Good luck! 🚀
