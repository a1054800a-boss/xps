#!/bin/bash
# XSpoof Installation Script for Termux + ADB
# Installs APK to connected Android device via ADB

set -e

echo "========================================"
echo "  XSpoof Installation Script (Termux)"
echo "========================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Check if ADB is available
if ! command -v adb &> /dev/null; then
    echo -e "${RED}Error: adb not found${NC}"
    echo "Install with: pkg install android-tools"
    exit 1
fi

echo -e "${GREEN}✓ ADB found${NC}"
echo ""

# Check if device is connected
echo -e "${YELLOW}Checking for connected devices...${NC}"
DEVICES=$(adb devices | grep -v "^List" | grep -v "^$" | wc -l)

if [ $DEVICES -eq 0 ]; then
    echo -e "${RED}Error: No Android devices connected${NC}"
    echo "1. Enable USB Debugging on your device"
    echo "2. Connect via USB cable"
    echo "3. Run: adb devices (to authorize)"
    exit 1
fi

echo -e "${GREEN}✓ Device connected${NC}"
adb devices
echo ""

# Find APK
echo -e "${YELLOW}Looking for APK...${NC}"
APK_PATH="app/build/outputs/apk/release/app-release.apk"

if [ ! -f "$APK_PATH" ]; then
    echo -e "${RED}Error: APK not found at $APK_PATH${NC}"
    echo "Build first with: ./scripts/build-termux.sh"
    exit 1
fi

echo -e "${GREEN}✓ Found APK: $APK_PATH${NC}"
echo ""

# Install APK
echo -e "${YELLOW}Installing APK...${NC}"
adb install -r "$APK_PATH"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Installation Complete!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Next steps:"
echo "  1. Install Magisk module via Magisk Manager"
echo "  2. Reboot device"
echo "  3. Open XSpoof app to configure settings"
echo ""
echo "To push Magisk module to device:"
echo "  adb push xspoof-magisk.zip /sdcard/"
echo ""
