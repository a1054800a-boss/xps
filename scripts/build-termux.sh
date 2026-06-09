#!/bin/bash
# XSpoof Build Script for Termux
# Builds both Magisk module ZIP and Android APK

set -e

echo "========================================"
echo "  XSpoof Build Script for Termux"
echo "========================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if running in Termux
if [ ! -d "$PREFIX" ]; then
    echo -e "${RED}Error: This script must be run in Termux${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Running in Termux${NC}"
echo ""

# Step 1: Build Magisk Module
echo -e "${YELLOW}Step 1: Building Magisk Module...${NC}"
if [ ! -d "magisk_module" ]; then
    echo -e "${RED}Error: magisk_module/ directory not found${NC}"
    exit 1
fi

# Ensure scripts are executable
chmod +x magisk_module/post-fs-data.sh
chmod +x magisk_module/service.sh

cd magisk_module
zip -r ../xspoof-magisk.zip . -x "*.git*"
cd ..

echo -e "${GREEN}✓ Magisk module ZIP created: xspoof-magisk.zip${NC}"
echo ""

# Step 2: Check for Android SDK
echo -e "${YELLOW}Step 2: Checking Android SDK...${NC}"

if [ -z "$ANDROID_SDK_ROOT" ]; then
    # Try to set ANDROID_SDK_ROOT if not already set
    if [ -d "$HOME/Android/Sdk" ]; then
        export ANDROID_SDK_ROOT="$HOME/Android/Sdk"
    elif [ -d "$PREFIX/opt/android-sdk" ]; then
        export ANDROID_SDK_ROOT="$PREFIX/opt/android-sdk"
    else
        echo -e "${YELLOW}Warning: ANDROID_SDK_ROOT not set${NC}"
        echo "To install Android SDK in Termux:"
        echo "  pkg install android-sdk"
        echo "  export ANDROID_SDK_ROOT=\$PREFIX/opt/android-sdk"
        echo ""
        read -p "Continue without SDK? (y/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
fi

if [ -n "$ANDROID_SDK_ROOT" ]; then
    echo -e "${GREEN}✓ Android SDK found: $ANDROID_SDK_ROOT${NC}"
else
    echo -e "${YELLOW}Note: Building without SDK (config only)${NC}"
fi

echo ""

# Step 3: Build APK with Gradle
echo -e "${YELLOW}Step 3: Building Android APK...${NC}"

if ! command -v gradle &> /dev/null && ! [ -f "gradlew" ]; then
    echo -e "${RED}Error: Gradle not found${NC}"
    echo "To install: pkg install gradle"
    exit 1
fi

# Use gradlew if available, otherwise gradle
if [ -f "gradlew" ]; then
    GRADLE_CMD="./gradlew"
else
    GRADLE_CMD="gradle"
fi

echo "Running: $GRADLE_CMD build"
$GRADLE_CMD build

if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    echo -e "${GREEN}✓ APK built successfully: app/build/outputs/apk/release/app-release.apk${NC}"
else
    echo -e "${YELLOW}Warning: APK not found in expected location${NC}"
    echo "Searching for APK files..."
    find app/build -name "*.apk" -type f
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Build Complete!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Output files:"
echo "  1. Magisk Module: xspoof-magisk.zip"
echo "  2. APK: app/build/outputs/apk/release/app-release.apk"
echo ""
echo "Installation instructions:"
echo ""
echo "Method 1: ADB (Recommended)"
echo "  adb install -r app/build/outputs/apk/release/app-release.apk"
echo ""
echo "Method 2: Manual Installation"
echo "  1. Copy APK to device: adb push app/build/outputs/apk/release/app-release.apk /sdcard/"
echo "  2. Open file manager on device and install from /sdcard/"
echo ""
echo "Magisk Module Installation:"
echo "  1. Copy xspoof-magisk.zip to device"
echo "  2. Open Magisk Manager → Modules → Install from storage"
echo "  3. Select xspoof-magisk.zip"
echo "  4. Reboot device"
echo ""
