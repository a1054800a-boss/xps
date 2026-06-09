# XSpoof Magisk Module Build Guide

This guide explains how to build XSpoof as both a Magisk module and a standalone UI APK.

## Prerequisites

- Android Studio 2022.1 or later
- JDK 11 or higher
- Android SDK 33+
- Gradle 7.0+

## Project Structure

```
XSpoof/
├── app/                          # Android app source code
│   ├── src/main/
│   │   ├── kotlin/               # Kotlin source files
│   │   ├── res/                  # Resources (layouts, strings, etc)
│   │   └── AndroidManifest.xml   # App manifest
│   └── build.gradle              # App build configuration
├── magisk_module/                # Magisk module files
│   ├── module.prop               # Module metadata
│   ├── common/                   # System customization
│   │   └── system.prop           # System properties
│   ├── post-fs-data.sh           # Post-FS initialization
│   ├── service.sh                # Background service
│   └── README.md                 # Module documentation
└── build.gradle                  # Root build configuration
```

## Building the UI APK

### Step 1: Build Release APK

```bash
# Navigate to project root
cd XSpoof

# Build release APK
./gradlew clean build assembleRelease
```

### Step 2: Locate Built APK

The APK will be generated at:
```
app/build/outputs/apk/release/app-release.apk
```

Rename it to `XSpoof-v2.0.0.apk` for distribution.

## Installing on Android Device

### Prerequisite: Root Access with Magisk

1. Ensure your device has Magisk Manager installed
2. Enable developer mode on your device
3. Enable USB debugging

### Installation Steps

#### Option 1: Via ADB (Recommended)

```bash
# Connect device via USB
adb devices

# Install APK
adb install app/build/outputs/apk/release/app-release.apk

# Open app
adb shell am start -n com.example.xspoof/.ui.MainActivity
```

#### Option 2: Via Magisk Manager

1. Open Magisk Manager
2. Tap the + icon to install module
3. Select the Magisk module ZIP (see packaging section below)
4. Reboot device

## Packaging as Magisk Module

### Step 1: Create Module ZIP

```bash
# Navigate to magisk_module directory
cd magisk_module

# Create ZIP with proper structure
zip -r ../XSpoof-Magisk-v2.0.0.zip .
```

### Step 2: Module ZIP Structure

```
XSpoof-Magisk-v2.0.0.zip
├── module.prop              (metadata)
├── common/
│   └── system.prop
├── post-fs-data.sh
├── service.sh
└── README.md
```

## Configuring Spoofing

After installation:

1. **Launch XSpoof App**
   - Open the XSpoof app from your app drawer

2. **Configure Settings**
   - **General Tab**: Enable module, toggle "Spoof All Apps"
   - **Device Tab**: Set manufacturer, model, fingerprint, Android ID
   - **Network Tab**: Configure IPv4 and IPv6 addresses
   - **Apps Tab**: Select specific apps to spoof (if not spoofing all)

3. **Restart Device**
   - Reboot for changes to take effect

## Verification

### Check if Module is Working

```bash
# View Xposed logs
adb logcat | grep XSpoof

# Check build properties
adb shell getprop | grep ro.product.manufacturer

# Verify spoofed Android ID
adb shell settings get secure android_id
```

### Test Spoofing in App

Create a test app that reads:
```kotlin
Log.d("DeviceTest", "Manufacturer: ${Build.MANUFACTURER}")
Log.d("DeviceTest", "Model: ${Build.MODEL}")
Log.d("DeviceTest", "Fingerprint: ${Build.FINGERPRINT}")
```

If spoofing works, you should see the configured values.

## Troubleshooting

### Module Not Activating

1. Verify Xposed/LSPosed is installed
2. Enable module in LSPosed Manager
3. Check if target app is listed in LSPosed scope
4. Reboot device

### Spoofing Not Working

1. Check app permissions
2. Verify configuration is saved in app
3. Review logcat: `adb logcat | grep XSpoof`
4. Ensure app is in target apps list or "Spoof All Apps" is enabled
5. Try force-closing and relaunching app

### Build Errors

```bash
# Clean rebuild
./gradlew clean build

# Sync Gradle
./gradlew --refresh-dependencies

# Update dependencies
./gradlew dependencyUpdates
```

## Distribution

### For GitHub Releases

1. Build APK: `./gradlew assembleRelease`
2. Create ZIP for Magisk: `zip -r XSpoof-Magisk.zip magisk_module/`
3. Upload both files to GitHub releases:
   - `XSpoof-v2.0.0.apk` (UI app)
   - `XSpoof-Magisk-v2.0.0.zip` (Magisk module)

## Advanced Configuration

### Custom Hook Points

Edit `XSpoofModule.kt` to add more hook points:

```kotlin
// Example: Hook custom method
XposedHelpers.findAndHookMethod(
    targetClass,
    "customMethod",
    String::class.java,
    object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam?): Any? {
            return "spoofedValue"
        }
    }
)
```

### Persistent Configuration

Configurations are stored in:
```
/data/data/com.example.xspoof/shared_prefs/xspoof_config.xml
```

Format: JSON via Gson

## Performance Considerations

- Module uses lightweight hooking
- Minimal memory footprint (~2-5 MB)
- No background services consuming battery
- Hooks only activated for targeted apps

## Security Notes

- Requires root/Magisk to function
- App runs with module permissions
- No data collection or telemetry
- All configuration stored locally

## Support & Issues

- GitHub Issues: Report bugs and feature requests
- Documentation: Check README.md for common issues
- Logcat: Always check XSpoof logs for debugging

## Version History

### v2.0.0
- Magisk module implementation
- Jetpack Compose UI
- Material Design 3 theme
- Per-app and global spoofing
- Network (IPv4/IPv6) spoofing
- Device property spoofing

## License

MIT License - See LICENSE file
