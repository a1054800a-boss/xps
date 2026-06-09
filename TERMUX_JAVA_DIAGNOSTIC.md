# Termux JAVA_HOME Fix - Final Solution

## Your Error:
```
ERROR: JAVA_HOME is set to an invalid directory: /data/data/com.termux/files/usr/opt/openjdk
```

This means that path doesn't actually exist on your Termux installation.

## Solution: Find Your Actual Java Installation

### Step 1: Find Where Java Actually Is

```bash
# Check where java executable is
which java

# Shows something like: /data/data/com.termux/files/usr/bin/java
```

### Step 2: Find the JDK Root Directory

The JDK root is the directory **above** the `bin/` folder that contains `java`.

```bash
# List the parent directories
ls -la /data/data/com.termux/files/usr/

# Look for directories related to java/openjdk
ls -la /data/data/com.termux/files/usr/ | grep -i java
ls -la /data/data/com.termux/files/usr/ | grep -i openjdk

# Or search more broadly
find /data/data/com.termux/files/usr -name "java" -type d 2>/dev/null
```

### Step 3: Set Correct JAVA_HOME

Based on what you find, set JAVA_HOME to the **root directory** (the one above `bin/`):

```bash
# If you found java in /data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk

# OR if it's just in /data/data/com.termux/files/usr
export JAVA_HOME=/data/data/com.termux/files/usr

# Verify it worked
java -version
echo $JAVA_HOME
```

---

## Quick Diagnostic Commands

Run these one by one to find the right path:

```bash
# 1. Check java exists
which java

# 2. Check lib/jvm
ls -la /data/data/com.termux/files/usr/lib/jvm/

# 3. Check opt
ls -la /data/data/com.termux/files/usr/opt/

# 4. Try setting JAVA_HOME to lib/jvm version
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
java -version

# If that fails, try:
export JAVA_HOME=/data/data/com.termux/files/usr
java -version
```

---

## Most Likely Fix

Based on Termux Java installations, try this:

```bash
cd ~/projects/xps

# Set Java to the most common Termux location
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk

# Also set these
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT

# Verify all work
java -version
gradle -version

# Now try build
./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"
```

---

## Add to .bashrc (Make Permanent)

Once you find the correct path, add it to your `.bashrc`:

```bash
# Edit bashrc
nano ~/.bashrc

# Add these lines at the end:
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export ANDROID_SDK_ROOT=$PREFIX/opt/android-sdk
export ANDROID_HOME=$ANDROID_SDK_ROOT

# Save: Ctrl+X → Y → Enter

# Reload
source ~/.bashrc
```

---

## Tell Me Your Output

Run this and show me the output:

```bash
which java
ls -la /data/data/com.termux/files/usr/lib/jvm/
ls -la /data/data/com.termux/files/usr/opt/
```

Then I'll tell you the exact JAVA_HOME to use!

---

## Temporary Workaround

If nothing works, try building **without** setting JAVA_HOME:

```bash
cd ~/projects/xps

# Unset JAVA_HOME
unset JAVA_HOME

# Try build with explicit Java path
/data/data/com.termux/files/usr/bin/java -version

./gradlew clean assembleRelease -x lint --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"
```

---

**Run the diagnostic commands and share the output so I can give you the exact fix!** 🔧
