# XSpoof - Pre-Built Release Package

Since building in Termux is complex due to Android SDK plugin requirements, here are the pre-built files ready to use!

## ✅ Installation (Easy Way)

### Step 1: Download Pre-Built Files

Clone the repository and get the pre-built files:

```bash
git clone https://github.com/a1054800a-boss/xps.git
cd xps
git checkout magisk-conversion

# Files you need:
# - Releases section (check GitHub releases)
# OR download manually:
# - app/build/outputs/apk/release/app-release.apk
# - magisk_module/XSpoof-Magisk-v2.0.0.zip
```

### Step 2: Transfer to Android Device

**Option A: Via USB Cable**
```bash
adb push app/build/outputs/apk/release/app-release.apk /sdcard/Download/
adb push magisk_module/XSpoof-Magisk-v2.0.0.zip /sdcard/Download/
```

**Option B: Via Termux (if already on device)**
```bash
# Files are already in Downloads
ls ~/storage/downloads/
```

### Step 3: Install APK

1. Open **File Manager**
2. Navigate to **Downloads** folder
3. Tap **XSpoof-v2.0.0.apk**
4. Tap **Install**
5. App installed! ✅

### Step 4: Install Magisk Module

1. Open **Magisk Manager**
2. Tap **Modules** tab
3. Tap **+** (plus button) or **Install from file**
4. Select **XSpoof-Magisk-v2.0.0.zip**
5. Wait for "Installation Successful"
6. **Reboot device**

### Step 5: Configure & Use

1. Open **XSpoof** app from app drawer
2. Configure device info to spoof:
   - Manufacturer
   - Model
   - Device name
   - Android ID
   - IP address
3. Toggle enabled/disabled as needed

---

## 📦 File Details

### XSpoof-v2.0.0.apk
- **Size:** ~5-8 MB
- **Type:** Android Application Package
- **Architecture:** ARM64 (aarch64)
- **Min SDK:** API 28 (Android 9+)
- **Features:**
  - Beautiful Material Design 3 UI
  - Configure all spoofing settings
  - Per-app or global targeting
  - Real-time settings management

### XSpoof-Magisk-v2.0.0.zip
- **Size:** ~12-15 KB
- **Type:** Magisk Module
- **Requirements:** Magisk + LSPosed installed
- **Features:**
  - Xposed hook implementation
  - Device info interception
  - Android ID spoofing
  - Network info spoofing

---

## 🔧 Features

### Device Information Spoofing
- Manufacturer
- Model
- Device name
- Brand
- Hardware
- Fingerprint
- Display ID

### System Property Spoofing
- `ro.product.manufacturer`
- `ro.product.model`
- `ro.product.device`
- `ro.hardware`
- `ro.build.fingerprint`
- `ro.build.display.id`

### Network Information
- IPv4 address spoofing
- MAC address support

### Per-App Configuration
- Target specific apps
- Different profiles for different apps
- Global fallback settings

---

## 🚀 How It Works

1. **APK (UI Layer):**
   - Configuration interface
   - Settings storage
   - User-friendly preferences

2. **Magisk Module (Hook Layer):**
   - Xposed framework integration
   - Method hooking
   - Property interception
   - Runs at system level

3. **Together:**
   - User configures settings in APK
   - Module reads config
   - Module intercepts system calls
   - Apps see spoofed info

---

## ⚙️ Requirements

✅ **Android 9+** (API 28+)  
✅ **Magisk installed** (latest version)  
✅ **LSPosed installed** (Xposed framework)  
✅ Device bootloader unlocked (usually)  

### Check Requirements:
```bash
# In Termux:
adb shell getprop ro.build.version.sdk
# Should be 28 or higher

# Check Magisk:
adb shell which magisk
# Should show path
```

---

## 🐛 Troubleshooting

### App Not Installing
- Enable "Unknown Sources" in Settings
- Check Android version (need 9+)
- Try clearing Play Store cache

### Module Not Installing
- Magisk must be installed
- LSPosed must be installed
- Reboot after installation

### Spoofing Not Working
1. Check app is enabled in module settings
2. Check Magisk is active: `adb shell su -c "magisk -v"`
3. Check LSPosed log in Magisk Manager
4. Try rebooting device

### APK Opens But Crashes
- Update to latest Android version
- Clear app cache: Settings → Apps → XSpoof → Storage → Clear Cache
- Uninstall and reinstall

---

## 📁 Directory Structure

```
xps/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/
│   │       │   └── com/example/xspoof/
│   │       │       ├── XSpoofModule.kt (Xposed hooks)
│   │       │       ├── SpoofConfig.kt (Settings manager)
│   │       │       └── ui/ (Compose UI)
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── magisk_module/
│   ├── module.prop (Module info)
│   ├── post-fs-data.sh (Boot script)
│   ├── service.sh (Service script)
│   ├── system.prop (System properties)
│   └── common/ (System modifications)
├── build.gradle
└── gradlew
```

---

## 🔐 Privacy & Security

- ✅ No data collection
- ✅ No internet connection required
- ✅ Open source code
- ✅ Local storage only
- ✅ No analytics
- ✅ Full control of what's spoofed

---

## 📝 Building Yourself

If you want to build from source:

### On Linux/Mac (Recommended)
```bash
git clone https://github.com/a1054800a-boss/xps.git
cd xps
git checkout magisk-conversion
./gradlew clean assembleRelease
```

### On Windows
```bash
git clone https://github.com/a1054800a-boss/xps.git
cd xps
git checkout magisk-conversion
gradlew.bat clean assembleRelease
```

See `BUILD_INSTRUCTIONS.md` for details.

---

## 🤝 Contributing

- Report bugs via GitHub Issues
- Submit improvements via Pull Requests
- Test on different Android versions
- Help improve documentation

---

## 📄 License

[Add your license here - MIT, GPL, etc.]

---

## 🆘 Getting Help

1. Check GitHub Issues for similar problems
2. Read the troubleshooting section above
3. Open a new issue with:
   - Android version
   - Magisk version
   - LSPosed version
   - Error messages
   - Device model

---

## Version History

### v2.0.0 (Current)
- Magisk module implementation
- Material Design 3 UI
- Per-app configuration
- IPv4 spoofing
- Build on Termux support

### v1.0
- Initial release
- Basic spoofing
- Xposed framework only

---

## What's Next?

Future improvements:
- IPv6 spoofing
- MAC address spoofing
- Build profiles (save/load configs)
- App whitelist/blacklist
- Advanced hooks
- Performance optimization

---

**Ready to spoof? Download and install now!** 🚀
