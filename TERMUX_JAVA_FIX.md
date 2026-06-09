# XSpoof Termux Build - JAVA_HOME Fix

## Your Current Error

```
ERROR: JAVA_HOME is set to an invalid directory: /data/data/com.termux/files/usr/opt/openjdk
```

**This happens because:** Gradle can't find the JAVA_HOME directory, even though Java works.

## Quick Fix - Run These Commands

```bash
cd ~/projects/xps

# Find where Java actually is
which java
# Shows: /data/data/com.termux/files/usr/bin/java

# Set JAVA_HOME to /data/data/com.termux/files/usr (without bin/)
export JAVA_HOME=/data/data/com.termux/files/usr

# Verify
ls -la /data/data/com.termux/files/usr/bin/java
java -version  # Should work
```

Now try building:

```bash
./gradlew clean assembleRelease -x lint --no-daemon
```

---

## If That Still Doesn't Work - Try This Alternative

```bash
cd ~/projects/xps

# Don't set JAVA_HOME at all, let gradle find it
unset JAVA_HOME

# Build without it
./gradlew clean assembleRelease -x lint --no-daemon
```

---

## Another Option - Use Different Java Setup

```bash
cd ~/projects/xps

# Try this exact export
export JAVA_HOME=$(dirname $(dirname $(which java)))

# Verify
echo $JAVA_HOME
java -version

# Then build
./gradlew clean assembleRelease -x lint --no-daemon
```

---

## Complete Working Script

Save as `~/build-fix.sh`:

```bash
#!/bin/bash
set -e

cd ~/projects/xps

echo "🔧 Setting up Java..."

# Method 1: Find java dynamically
export JAVA_HOME=$(dirname $(dirname $(which java)))
echo "JAVA_HOME set to: $JAVA_HOME"

# Verify Java works
java -version

echo "✅ Java is ready"
echo ""
echo "🔨 Building XSpoof..."

# Build with explicit settings
./gradlew clean assembleRelease -x lint --no-daemon

echo ""
echo "✅ Build complete!"
```

**Run it:**
```bash
chmod +x ~/build-fix.sh
~/build-fix.sh
```

---

## What To Do Right Now

1. **Stop current process** (if still running): Press `Ctrl+C`

2. **Run this:**
```bash
cd ~/projects/xps

# Set JAVA_HOME correctly
export JAVA_HOME=/data/data/com.termux/files/usr

# Verify Java works
java -version
# Should print version info

# Test gradle can find Java
./gradlew -version
# Should print Gradle version
```

3. **If both commands work**, then run build:
```bash
./gradlew clean assembleRelease -x lint --no-daemon
```

---

## Why This Happens

- `java -version` works because it's just running the executable
- Gradle needs the full **JDK directory structure** (not just the `java` binary)
- The path is: `/data/data/com.termux/files/usr/` (contains `bin/`, `lib/`, etc.)

---

## Troubleshooting

### Still getting the error?

```bash
# Check what Java dirs exist
ls -la /data/data/com.termux/files/usr/ | grep -i java

# Try finding the actual openjdk dir
find /data/data/com.termux/files -name "openjdk*" -type d 2>/dev/null

# If found, use that path
export JAVA_HOME=/path/from/above
./gradlew -version
```

### Build still fails?

```bash
# Try gradle directly without gradlew
gradle clean assembleRelease -x lint --no-daemon
```

### Memory issues?

```bash
# Reduce gradle memory
export GRADLE_OPTS="-Xmx256m"
export JAVA_HOME=/data/data/com.termux/files/usr
./gradlew clean assembleRelease -x lint --no-daemon --no-build-cache
```

---

## Summary

**Just use this one command:**

```bash
cd ~/projects/xps && export JAVA_HOME=/data/data/com.termux/files/usr && ./gradlew clean assembleRelease -x lint --no-daemon
```

That should work! Let me know if it builds successfully. 🚀
