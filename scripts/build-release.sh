#!/bin/bash

# XSpoof Build and Release Script
# This script builds the APK and creates the Magisk module ZIP

set -e

echo "🔨 Starting XSpoof Build..."

# Setup
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Build APK
echo "📦 Building APK..."
chmod +x ./gradlew
./gradlew clean assembleRelease -x lint

# Create Magisk Module ZIP
echo "📦 Creating Magisk Module ZIP..."
cd magisk_module
zip -r ../XSpoof-Magisk-v2.0.0.zip module.prop common/ post-fs-data.sh service.sh uninstall.sh README.md
cd ..

# Verify files
echo "✅ Build Complete!"
echo ""
echo "📁 Output Files:"
ls -lah app/build/outputs/apk/release/app-release.apk
ls -lah XSpoof-Magisk-v2.0.0.zip

echo ""
echo "🎉 Ready for release!"
