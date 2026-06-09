# Build XSpoof APK in Termux on Android (WORKING METHOD)

Since you have Termux on Android, we can build directly on your phone! Here's the working method.

## Key Difference This Time

We'll **install the full Android SDK in Termux** so Gradle can find the Android plugins.

## Step 1: Update Termux & Install Tools

```bash
# Update packages
pkg update && pkg upgrade -y

# Install essential tools
pkg install -y git openjdk-17 gradle android-tools zip wget python

# Verify installations
java -version
gradle -version
git --version
```

## Step 2: Install Android SDK in Termux

```bash
# Install Android SDK
pkg install -y android-sdk

# Set environment variables
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
export JAVA_HOME=$PREFIX/opt/openjdk

# Verify SDK
ls -la $ANDROID_SDK_ROOT/

# Should show: build-tools/, platforms/, tools/, etc.
```

## Step 3: Make Java Home Permanent

```bash
# Add to .bashrc so it's always set
cat >> ~/.bashrc << 'EOF'

# Android SDK paths
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
export JAVA_HOME=$PREFIX/opt/openjdk
export PATH=$PATH:$ANDROID_SDK_ROOT/tools:$ANDROID_SDK_ROOT/platform-tools
EOF

# Reload bashrc
source ~/.bashrc

# Verify
echo $ANDROID_SDK_ROOT
java -version
gradle -version
```

## Step 4: Clone Repository

```bash
# Create projects directory
mkdir -p ~/projects
cd ~/projects

# Clone the repo
git clone https://github.com/a1054800a-boss/xps.git
cd xps

# Switch to magisk-conversion branch
git checkout magisk-conversion

# Verify directory
pwd
# Should show: /data/data/com.termux/files/home/projects/xps

ls -la
# Should show: build.gradle, app/, magisk_module/, etc.
```

## Step 5: Create gradlew Script

```bash
cd ~/projects/xps

# Create gradlew
cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF

# Make executable
chmod +x gradlew

# Verify
ls -la gradlew
./gradlew -version
```

## Step 6: Build APK (THE IMPORTANT PART)

```bash
cd ~/projects/xps

# Set environment variables
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
export JAVA_HOME=$PREFIX/opt/openjdk

# Build with explicit Android SDK
./gradlew clean assembleRelease \
  -x lint \
  --no-daemon \
  -Dorg.gradle.jvmargs="-Xmx512m"

# This will take 15-25 minutes on first build
# Be PATIENT - don't interrupt!
```

**Watch for:**
```
> Task :app:compileReleaseKotlin
> Task :app:packageReleaseResources
> Task :app:assembleReleaseResources
BUILD SUCCESSFUL in XXm XXs
```

## Step 7: Verify APK Was Created

```bash
# Check if APK exists
ls -lah ~/projects/xps/app/build/outputs/apk/release/

# Should show: app-release.apk (5-8 MB)
```

## Step 8: Create Magisk Module ZIP

```bash
cd ~/projects/xps/magisk_module

# Verify you're in correct directory
pwd
# Should end with: /magisk_module

# List files
ls -la

# Create ZIP
zip -r ../XSpoof-Magisk-v2.0.0.zip module.prop common/ post-fs-data.sh service.sh uninstall.sh README.md

# Go back and verify
cd ..
ls -lah XSpoof-Magisk-v2.0.0.zip
# Should show size like 12K-15K
```

## Step 9: Copy to Downloads

```bash
# Create downloads folder if needed
mkdir -p ~/storage/downloads

# Copy APK
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk

# Copy ZIP
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

# Verify both files
ls -lah ~/storage/downloads/XSpoof*
```

## Step 10: Install on Device

**Install APK:**
1. Open File Manager
2. Go to `/sdcard/Download/` or `/storage/emulated/0/Download/`
3. Tap `XSpoof-v2.0.0.apk`
4. Install

**Install Magisk Module:**
1. Open Magisk Manager
2. Modules tab → + button
3. Select `XSpoof-Magisk-v2.0.0.zip`
4. Wait for "Installation Successful"
5. Reboot device

---

## All-In-One Setup Script

Save as `~/setup-build.sh`:

```bash
#!/bin/bash
set -e

echo "🔧 Setting up XSpoof build environment..."

# Install packages
pkg install -y git openjdk-17 gradle android-tools zip wget python

# Set paths in bashrc
cat >> ~/.bashrc << 'EOF'

# Android SDK paths
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
export JAVA_HOME=$PREFIX/opt/openjdk
export PATH=$PATH:$ANDROID_SDK_ROOT/tools:$ANDROID_SDK_ROOT/platform-tools
EOF

# Reload
source ~/.bashrc

# Clone if not exists
if [ ! -d ~/projects/xps ]; then
    mkdir -p ~/projects
    cd ~/projects
    git clone https://github.com/a1054800a-boss/xps.git
    cd xps
    git checkout magisk-conversion
fi

cd ~/projects/xps

# Create gradlew
cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF
chmod +x gradlew

echo "✅ Setup complete!"
echo ""
echo "Next, run the build:"
echo "cd ~/projects/xps && ./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs=\"-Xmx512m\""
```

Run it:
```bash
chmod +x ~/setup-build.sh
~/setup-build.sh
```

---

## Build Script (Automated)

Save as `~/build-xspoof.sh`:

```bash
#!/bin/bash
set -e

echo "🔨 Building XSpoof..."

# Set environment
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
export JAVA_HOME=$PREFIX/opt/openjdk

cd ~/projects/xps

# Build
echo "📦 Building APK (this takes 15-25 minutes)..."
./gradlew clean assembleRelease \
  -x lint \
  --no-daemon \
  -Dorg.gradle.jvmargs="-Xmx512m"

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

# Create ZIP
echo "📦 Creating Magisk module..."
cd magisk_module
zip -r ../XSpoof-Magisk-v2.0.0.zip module.prop common/ post-fs-data.sh service.sh uninstall.sh README.md
cd ..

# Copy files
echo "📁 Copying to Downloads..."
mkdir -p ~/storage/downloads
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

echo ""
echo "✅ Build complete!"
echo ""
echo "📁 Files ready:"
ls -lah ~/storage/downloads/XSpoof*
```

Run it:
```bash
chmod +x ~/build-xspoof.sh
~/build-xspoof.sh
```

---

## Troubleshooting for Termux

### "Plugin [id: 'com.android.application'] was not found"

**Fix:** You need the Android SDK installed:
```bash
pkg install -y android-sdk

# Verify it's there
ls -la $PREFIX/opt/android-sdk/
```

### "JAVA_HOME is set to an invalid directory"

**Fix:** Make sure you set it correctly:
```bash
# Check what's actually there
ls -la $PREFIX/opt/openjdk/bin/java

# Then set it right
export JAVA_HOME=$PREFIX/opt/openjdk

# NOT $PREFIX/opt/openjdk/bin or other variations
```

### Build takes too long (over 30 minutes)

This is **normal on first build**. Termux is slower than Linux. Just wait.

If it seems stuck after 30+ minutes:
```bash
# Check process
ps aux | grep gradle

# If really stuck
pkill -f gradle

# Try again with less memory
./gradlew clean assembleRelease \
  -x lint \
  --no-daemon \
  -Dorg.gradle.jvmargs="-Xmx256m"
```

### Out of memory error

Reduce Gradle memory:
```bash
./gradlew clean assembleRelease \
  -x lint \
  --no-daemon \
  -Dorg.gradle.jvmargs="-Xmx256m"
```

### ZIP creation fails

Make sure you're in the right directory:
```bash
pwd
# Must end with: /magisk_module

ls -la
# Must show: module.prop, common/, post-fs-data.sh, etc.
```

---

## Quick Termux Command Summary

```bash
# One-time setup
pkg install -y git openjdk-17 gradle android-tools zip
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT
export JAVA_HOME=$PREFIX/opt/openjdk

# Clone
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
./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"

# Create ZIP
cd magisk_module && zip -r ../XSpoof-Magisk-v2.0.0.zip . && cd ..

# Copy
mkdir -p ~/storage/downloads
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

# Verify
ls -lah ~/storage/downloads/XSpoof*
```

---

## Key Points

✅ **Install Android SDK** - This is the missing piece!  
✅ **Set JAVA_HOME** correctly  
✅ **Be patient** - First build takes 20+ minutes  
✅ **Check each step** - Verify directories exist  
✅ **Use the automation scripts** - Easier than copy-pasting

---

## You've Got This! 🚀

Try this method - it should work now that we have the Android SDK installed!

Let me know if you hit any errors and we'll fix them together.
