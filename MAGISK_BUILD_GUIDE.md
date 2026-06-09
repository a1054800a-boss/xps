# Building XSpoof as Magisk Module + APK

## Architecture Overview

XSpoof now consists of two components:

### 1. Magisk Module (`magisk_module/`)
- **post-fs-data.sh**: Reads config and applies device property overrides
- **service.sh**: Runtime service for dynamic property updates
- **system.prop**: Property definitions
- **module.prop**: Magisk module metadata

### 2. Configuration APK
- Settings UI (Jetpack Compose)
- Writes config to `/data/adb/modules/xspoof/config.json`
- No Xposed framework required

## Building the Magisk Module

### Prerequisites
- Android device with Magisk installed
- Magisk Manager or similar app

### Steps

1. **Create Module ZIP**:
   ```bash
   cd magisk_module/
   zip -r ../xspoof-magisk.zip .
   ```

2. **Install via Magisk Manager**:
   - Open Magisk Manager
   - Tap "Modules"
   - Select "Install from storage"
   - Choose `xspoof-magisk.zip`
   - Reboot device

3. **Verify Installation**:
   ```bash
   adb shell ls -la /data/adb/modules/xspoof/
   adb shell cat /data/adb/modules/xspoof/module.prop
   ```

## Building the APK

### Prerequisites
- Android Studio (latest)
- JDK 11+
- Android SDK API 28+

### Steps

1. **Update AndroidManifest.xml**:
   - Remove Xposed-specific metadata
   - Keep only the configuration activity

2. **Update SpoofConfig.kt**:
   - Change config file path from SharedPreferences to:
     ```kotlin
     /data/adb/modules/xspoof/config.json
     ```
   - Add proper file permissions handling

3. **Build APK**:
   ```bash
   ./gradlew build
   # Output: app/build/outputs/apk/release/app-release.apk
   ```

4. **Install APK**:
   ```bash
   adb install -r app/build/outputs/apk/release/app-release.apk
   ```

## Configuration Flow

```
APK (UI) → /data/adb/modules/xspoof/config.json
                         ↓
                   (post-fs-data.sh reads)
                         ↓
              system.prop overrides applied
                         ↓
              Device properties spoofed system-wide
```

## Configuration File Format

Path: `/data/adb/modules/xspoof/config.json`

```json
{
  "enabled": true,
  "spoof_all": false,
  "target_apps": ["com.example.app"],
  "manufacturer": "Google",
  "model": "Pixel 6",
  "fingerprint": "google/oriole/oriole:12/S2B2.220816.016:user/release-keys",
  "device": "oriole",
  "product": "oriole",
  "brand": "google",
  "hardware": "oriole",
  "display_id": "S2B2.220816.016.user",
  "android_id": "a1b2c3d4e5f6g7h8",
  "ipv4": "192.0.2.123",
  "ipv6": "2001:db8::1"
}
```

## Important Notes

1. **Permissions**:
   - APK must have write access to `/data/adb/modules/xspoof/`
   - May require SELinux policy modifications

2. **Reboot Required**:
   - Changes to device properties require reboot
   - Property changes in post-fs-data.sh take effect after reboot

3. **Target Apps**:
   - Property-level spoofing affects all apps
   - Cannot selectively spoof per-app (limitation vs Xposed)
   - Use `spoof_all: true` or list specific apps (advisory only)

4. **Android ID**:
   - Cannot be truly spoofed via system properties
   - Requires additional hooks (SQLite database modification)
   - Current implementation logs support for future enhancement

5. **IP Address Spoofing**:
   - Network-level spoofing requires netfilter rules
   - Beyond scope of basic property override
   - Can be added via custom iptables rules in service.sh

## Troubleshooting

### Module not applying properties

```bash
# Check Magisk installation
adb shell magisk --version

# View module log
adb shell cat /data/adb/modules/xspoof/xspoof.log

# Check config file
adb shell cat /data/adb/modules/xspoof/config.json

# Verify properties are set
adb shell getprop ro.product.manufacturer
adb shell getprop ro.product.model
```

### APK cannot write to module directory

```bash
# Check permissions
adb shell ls -la /data/adb/modules/xspoof/

# Grant write permissions
adb shell chmod 777 /data/adb/modules/xspoof/
adb shell chmod 666 /data/adb/modules/xspoof/config.json
```

### Properties revert after reboot

- Ensure config.json exists with correct values
- Check post-fs-data.sh is executable:
  ```bash
  adb shell chmod +x /data/adb/modules/xspoof/post-fs-data.sh
  ```

## Future Enhancements

1. **Per-App Spoofing**: Modify Magisk module to hook specific apps
2. **Android ID Database**: Hook SQLite to modify actual Android ID
3. **Network Spoofing**: Add netfilter-based IP spoofing
4. **App Compatibility Database**: Store app-specific bypass strategies
5. **Cloud Sync**: Sync config across devices

## References

- [Magisk Module Development](https://topjohnwu.github.io/Magisk/guides.html)
- [Android System Properties](https://developer.android.com/reference/android/os/Build)
- [Magisk Module Template](https://github.com/topjohnwu/magisk-module-installer)
