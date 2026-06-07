# XSpoof - Xposed/LSPosed Device Spoofer Module

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive **Xposed/LSPosed module** that allows you to spoof critical device information for any installed app on your Android device. Perfect for development, testing, and privacy purposes.

## 🎯 Features

✅ **Device Information Spoofing**
- Manufacturer (e.g., "Google")
- Model (e.g., "Pixel 6")
- Device name
- Product name
- Brand
- Hardware
- Display ID

✅ **Identification Spoofing**
- Android ID
- Build Fingerprint
- Build Properties (via `System.getProperty()`)

✅ **Network Spoofing**
- IPv4 Address
- IPv6 Address (framework in place)

✅ **App-Specific Targeting**
- Target specific apps or all apps
- Selective spoofing per package

✅ **User-Friendly UI**
- Material Design 3 interface with Jetpack Compose
- Tabbed configuration panels
- Easy-to-use settings management
- Add/remove target apps dynamically

## 📋 Prerequisites

- **Android 9+ (API 28+)**
- **Magisk** with **LSPosed** or **Xposed Framework** installed
- **Android Studio** (for building)
- Basic knowledge of Android development

## 🚀 Installation

### Option 1: Build from Source

```bash
# Clone the repository
git clone https://github.com/carlahenrikssen-web/XSpoof.git
cd XSpoof

# Build the APK
./gradlew build

# The APK will be in: app/build/outputs/apk/release/app-release.apk
```

### Option 2: Install Pre-built APK

1. Download the latest release APK
2. Install on your device (requires developer mode)
3. Open LSPosed manager
4. Enable the XSpoof module
5. Select target apps
6. **Reboot device**

## 📖 Usage

### Configuration Tabs

#### 1. **General Tab**
- Enable/disable the module
- Toggle "Spoof All Apps" mode (applies to all installed apps)

#### 2. **Device Info Tab**
- Set custom manufacturer, model, fingerprint
- Configure device properties (brand, hardware, etc.)
- Customize Android ID

#### 3. **Network Tab**
- Configure spoofed IPv4 address
- Configure spoofed IPv6 address

#### 4. **Target Apps Tab**
- Add specific apps to spoof for
- Remove apps from the target list
- (If "Spoof All Apps" is enabled, targets list is ignored)

### Example Configurations

**Spoof as Google Pixel 6:**
```
Manufacturer: Google
Model: Pixel 6
Brand: google
Device: oriole
Product: oriole
Fingerprint: google/oriole/oriole:12/S2B2.220816.016:user/release-keys
```

**Spoof as Samsung Galaxy S21:**
```
Manufacturer: Samsung
Model: SM-G991B
Brand: samsung
Device: o1s
Product: o1s
Fingerprint: samsung/o1s/o1s:12/S901BXXS1AUG1:user/release-keys
```

## 🔧 Architecture

```
XSpoof/
├── app/src/main/java/com/example/xspoof/
│   ├── XSpoofModule.kt          # Main Xposed hook implementation
│   ├── SpoofConfig.kt           # Configuration data & persistence
│   └── ui/
│       ├── MainActivity.kt      # Main UI activity
│       └── theme/               # Material Design 3 theme
├── app/src/main/
│   └── AndroidManifest.xml      # Xposed module metadata
└── build.gradle                 # Gradle configuration
```

### How It Works

1. **XSpoofModule** implements `IXposedHookLoadPackage`
2. On package load, it reads the configuration from SharedPreferences
3. If the package matches target criteria, it:
   - Hooks `Build` class static fields
   - Hooks `WifiInfo.getIpAddress()` for IPv4
   - Hooks `Settings.Secure.getString()` for Android ID
   - Hooks `System.getProperty()` for device properties
   - Hooks `LinkProperties` for IPv6 (advanced)

4. All hooked methods return spoofed values instead of real device info

## 📊 Configuration Persistence

Configuration is stored in:
```
/data/data/com.example.xspoof/shared_prefs/xspoof_config.xml
```

Format: JSON serialization via Gson

```json
{
  "enabled": true,
  "spoof_all": false,
  "target_apps": ["com.example.app"],
  "manufacturer": "Google",
  "model": "Pixel 6",
  "fingerprint": "google/oriole/oriole:12/S2B2.220816.016:user/release-keys",
  "android_id": "a1b2c3d4e5f6g7h8",
  "ipv4": "192.0.2.123",
  "ipv6": "2001:db8::1"
}
```

## ⚙️ Advanced Usage

### Hooking Additional Methods

To spoof additional device properties, add hooks in `hookDeviceProperties()` or `XSpoofModule.kt`:

```kotlin
// Example: Hook a custom method
XposedHelpers.findAndHookMethod(
    targetClass,
    "getCustomProperty",
    object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam?): Any? {
            return "spoofedValue"
        }
    }
)
```

### Debugging

View module logs:
```bash
adb logcat | grep XSpoof
```

## ⚠️ Important Notes

1. **Requires Reboot**: Changes take effect after device reboot
2. **LSPosed Required**: This is a LSPosed/Xposed module, not a standalone app
3. **App Compatibility**: Some apps may bypass spoofing via:
   - Native code (JNI)
   - Hardware-level APIs
   - Custom device checks
4. **Performance**: Minimal impact; uses efficient hooking
5. **Stability**: Thoroughly test on secondary devices first

## 🧪 Testing

Verify spoofing is working:

```kotlin
// In target app
Log.d("DeviceInfo", Build.MANUFACTURER)  // Should show spoofed value
Log.d("DeviceInfo", Build.MODEL)          // Should show spoofed value
Log.d("DeviceInfo", Build.FINGERPRINT)    // Should show spoofed value
```

## 📝 Logs

Module logs appear in logcat with `XSpoof:` prefix:

```
XSpoof: Hooking com.example.app
XSpoof: Build fields spoofed
XSpoof: IPv4 spoofing enabled (192.0.2.123)
XSpoof: Android ID spoofing enabled
```

## 🤝 Contributing

Contributions welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request

## 📄 License

MIT License - See LICENSE file for details

## ⚖️ Disclaimer

This module is for **educational, development, and testing purposes only**. Misuse to:
- Bypass app security measures
- Violate terms of service
- Commit fraud or deception

...is the sole responsibility of the user. The authors assume no liability.

## 🔗 References

- [Xposed Framework](https://github.com/rovo89/XposedBridge)
- [LSPosed Documentation](https://github.com/LSPosed/LSPosed)
- [Android Build Class](https://developer.android.com/reference/android/os/Build)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

**Made with ❤️ for Android developers and privacy enthusiasts**
