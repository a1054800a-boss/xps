# Termux Build - FINAL FIX for Your System

## Your Java Installation Found! ✅

From your output:
- Java 17: `/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk`
- Java 21: `/data/data/com.termux/files/usr/lib/jvm/java-21-openjdk`

## Build Command - COPY & PASTE THIS EXACTLY:

```bash
cd ~/projects/xps

# Set CORRECT paths for your system
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT

# Verify everything works
java -version
gradle -version

# BUILD!
./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"
```

**This should work now!** The issue was `$PREFIX/opt/openjdk` doesn't exist - you have it in `/lib/jvm/` instead.

---

## If You Want It Permanent (Add to .bashrc)

```bash
# Open bashrc
nano ~/.bashrc

# Add these lines at the very end:
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT

# Save: Ctrl+X → Y → Enter

# Reload
source ~/.bashrc
```

Then you can just run:
```bash
cd ~/projects/xps
./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"
```

---

## Complete Build Command (One Liner)

If you want everything in one command:

```bash
cd ~/projects/xps && export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk && export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk && export ANDROID_HOME=$ANDROID_SDK_ROOT && ./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"
```

---

## Build Automation Script

Save this as `~/build-xspoof-final.sh`:

```bash
#!/bin/bash
set -e

# Set paths for THIS system
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT

echo "✅ Java: $JAVA_HOME"
echo "✅ Android SDK: $ANDROID_SDK_ROOT"
echo ""

cd ~/projects/xps

echo "🔨 Building XSpoof APK..."
./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "📦 Creating Magisk ZIP..."
cd magisk_module
zip -r ../XSpoof-Magisk-v2.0.0.zip module.prop common/ post-fs-data.sh service.sh uninstall.sh README.md
cd ..

echo "📁 Copying files..."
mkdir -p ~/storage/downloads
cp app/build/outputs/apk/release/app-release.apk ~/storage/downloads/XSpoof-v2.0.0.apk
cp XSpoof-Magisk-v2.0.0.zip ~/storage/downloads/XSpoof-Magisk-v2.0.0.zip

echo ""
echo "✅ BUILD COMPLETE!"
echo ""
echo "📁 Your files:"
ls -lah ~/storage/downloads/XSpoof*
echo ""
echo "📱 Next: Install APK + Magisk module, reboot!"
```

Run it:
```bash
chmod +x ~/build-xspoof-final.sh
~/build-xspoof-final.sh
```

---

## Summary

**The fix:** Use `/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk` instead of `$PREFIX/opt/openjdk`

**Try building now!** It should work! 🚀
