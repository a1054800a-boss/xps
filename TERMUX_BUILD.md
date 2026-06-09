# XSpoof Build Guide for Termux (Android) - FIXED

## Quick Start (Copy & Paste)

### Step 1: Create Project Directory & Clone

```bash
# Go to home
cd ~

# Create projects folder
mkdir -p projects
cd projects

# Clone repository
git clone https://github.com/a1054800a-boss/xps.git
cd xps

# Verify you're in correct location
pwd
# Should show: /data/data/com.termux/files/home/projects/xps

# Switch branch
git checkout magisk-conversion
```

### Step 2: Install Missing Tools

```bash
# Go back to xps folder if needed
cd ~/projects/xps

# Install zip (may already be installed)
pkg install -y zip

# Verify all tools
which gradle
which java
which git
which zip
```

### Step 3: Set Up Java Home CORRECTLY

**Important:** Use `which java` to find correct path first:

```bash
# Find java installation
which java
# Shows: /data/data/com.termux/files/usr/bin/java

# Set JAVA_HOME to the directory above bin/
export JAVA_HOME=$PREFIX/opt/openjdk

# Verify Java works
java -version
# Should work without errors now
```

### Step 4: Verify You're in Correct Directory

```bash
# Make sure you're in xps directory
cd ~/projects/xps

# List files to confirm
ls -la
# Should show:
# build.gradle
# build.sh
# magisk_module/
# app/
# etc.
```

### Step 5: Create gradlew Script

```bash
# Make sure you're in ~/projects/xps
pwd

# Create the gradlew file
cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF

# Make it executable
chmod +x gradlew

# Verify it exists
ls -la gradlew
# Should show: -rwxr-xr-x gradlew
```

### Step 6: Build the APK (Takes 10-20 minutes)

```bash
# Make sure Java is exported
export JAVA_HOME=$PREFIX/opt/openjdk

# Go to xps directory
cd ~/projects/xps

# Start build
./gradlew clean assembleRelease -x lint --no-daemon

# You'll see output like:
# > Task :app:compileReleaseKotlin
# > Task :app:packageReleaseResources
# Eventually: BUILD SUCCESSFUL in 15m 30s
```

**If it hangs:** Wait 15+ minutes, don't interrupt!

### Step 7: Verify APK Was Created

```bash
# Check if APK exists
ls -la app/build/outputs/apk/release/

# Should show: app-release.apk (5-8 MB)

# Full path
ls -lah ~/projects/xps/app/build/outputs/apk/release/app-release.apk
```

### Step 8: Create Magisk Module ZIP

```bash
# Go to magisk_module directory
cd ~/projects/xps/magisk_module

# Verify you're in magisk_module
pwd
# Should show: /data/data/com.termux/files/home/projects/xps/magisk_module

# List what's in this folder
ls -la
# Should show:
# module.prop
# common/
# post-fs-data.sh
# service.sh
# README.md
# etc.

# Create ZIP from THIS directory
zip -r ../XSpoof-Magisk-v2.0.0.zip .

# Go back to xps root
cd ..

# Verify ZIP exists
ls -lah XSpoof-Magisk-v2.0.0.zip
# Should show size like 15K
```

### Step 9: Copy Files to Downloads

```bash
# Make sure downloads folder exists
mkdir -p ~/storage/downloads

# Go to xps directory
cd ~/projects/xps

# Copy APK
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk

# Copy ZIP
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

# Verify both files
ls -lah ~/storage/downloads/XSpoof*
# Should show:
# XSpoof-v2.0.0.apk
# XSpoof-Magisk-v2.0.0.zip
```

### Step 10: Verify Files in Android

```bash
# Check where files actually are
ls /sdcard/Download/

# Or via storage
ls ~/storage/downloads/
```

---

## Troubleshooting

### "No such file or directory" for projects/xps

**Fix:**
```bash
# Create it fresh
cd ~
rm -rf projects  # Remove if it exists
mkdir -p projects
cd projects
git clone https://github.com/a1054800a-boss/xps.git
cd xps
```

### "JAVA_HOME is set to an invalid directory"

**Fix:**
```bash
# Don't use the full path, use $PREFIX
export JAVA_HOME=$PREFIX/opt/openjdk

# Verify
java -version
# Should work now

# Make sure this is set for every command
export JAVA_HOME=$PREFIX/opt/openjdk && ./gradlew assembleRelease -x lint --no-daemon
```

### "zip warning: name not matched"

This means you're not in the `magisk_module` directory. **Fix:**
```bash
# Verify current location
pwd
# Should end with: /magisk_module

# If not, navigate there
cd ~/projects/xps/magisk_module

# List files
ls -la

# Then create ZIP
zip -r ../XSpoof-Magisk-v2.0.0.zip .
```

### Build takes too long (over 20 minutes)

**Normal on first build!** Just wait. If it's been 30+ minutes:
```bash
# Check if process is running
ps aux | grep gradle

# If stuck, stop it
pkill -f gradle

# Try with less memory
export GRADLE_OPTS="-Xmx256m"
./gradlew assembleRelease -x lint --no-daemon --no-build-cache
```

### Cannot find app-release.apk

**The build failed.** Check:
```bash
# Look for errors
./gradlew assembleRelease --stacktrace 2>&1 | tail -50

# Or check build log
find ~/projects/xps -name "*.log" -type f
```

---

## One-Line Copy-Paste Commands

**If you're in the right directory**, just run each line:

```bash
cd ~ && mkdir -p projects && cd projects && git clone https://github.com/a1054800a-boss/xps.git && cd xps && git checkout magisk-conversion
```

```bash
export JAVA_HOME=$PREFIX/opt/openjdk && ./gradlew clean assembleRelease -x lint --no-daemon
```

```bash
cd ~/projects/xps/magisk_module && zip -r ../XSpoof-Magisk-v2.0.0.zip . && cd ..
```

```bash
mkdir -p ~/storage/downloads && cp ~/projects/xps/app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk && cp ~/projects/xps/XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip
```

```bash
ls -lah ~/storage/downloads/XSpoof*
```

---

## Automated Build Script

Save this as `~/build.sh`:

```bash
#!/bin/bash

echo "🔨 Building XSpoof..."

# Ensure directory structure
cd ~
mkdir -p projects
cd projects

# Clone if not exists
if [ ! -d "xps" ]; then
    echo "📡 Cloning repository..."
    git clone https://github.com/a1054800a-boss/xps.git
fi

cd xps

# Setup
echo "⚙️ Setting up..."
git checkout magisk-conversion 2>/dev/null || true
git pull origin magisk-conversion 2>/dev/null || true

# Create gradlew if missing
if [ ! -f "gradlew" ]; then
    echo "📝 Creating gradlew..."
    cat > gradlew << 'EOF'
#!/bin/bash
gradle "$@"
EOF
    chmod +x gradlew
fi

# Set Java
export JAVA_HOME=$PREFIX/opt/openjdk

# Build
echo "📦 Building APK... (this takes 10-20 minutes)"
./gradlew clean assembleRelease -x lint --no-daemon

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Create ZIP
echo "📦 Creating Magisk module..."
cd magisk_module
zip -r ../XSpoof-Magisk-v2.0.0.zip .
cd ..

# Copy files
echo "📁 Copying to Downloads..."
mkdir -p ~/storage/downloads
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

echo ""
echo "✅ Done!"
echo ""
echo "📁 Files ready:"
ls -lah ~/storage/downloads/XSpoof*
echo ""
echo "📱 Install these files:"
echo "  1. XSpoof-v2.0.0.apk → via file manager"
echo "  2. XSpoof-Magisk-v2.0.0.zip → via Magisk Manager"
echo "  3. Reboot"
```

**Run it:**
```bash
chmod +x ~/build.sh
~/build.sh
```

---

## Key Paths to Remember

```
Termux Home:           ~/  or  /data/data/com.termux/files/home/
Project:               ~/projects/xps/
APK after build:       ~/projects/xps/app/build/outputs/apk/release/app-release.apk
Module before ZIP:     ~/projects/xps/magisk_module/
Magisk ZIP:            ~/projects/xps/XSpoof-Magisk-v2.0.0.zip
Downloads (for phone): ~/storage/downloads/  or  /sdcard/Download/
```

---

## Still Having Issues?

1. **Verify each step worked:**
   ```bash
   pwd                    # Are you in ~/projects/xps/?
   ls -la gradlew         # Does gradlew exist?
   java -version          # Does Java work?
   ./gradlew -v           # Does gradle work?
   ```

2. **Check available space:**
   ```bash
   df -h                  # Need 2GB+ free
   free -h                # Need 512MB+ RAM
   ```

3. **Try fresh clone:**
   ```bash
   cd ~
   rm -rf projects
   mkdir -p projects && cd projects
   git clone https://github.com/a1054800a-boss/xps.git
   cd xps
   git checkout magisk-conversion
   ```

Good luck! 🚀
