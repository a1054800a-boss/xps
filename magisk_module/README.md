# XSpoof Magisk Module

A Magisk module version of XSpoof that allows you to spoof device information globally or per-app.

## Installation

1. Download the Magisk Manager app
2. Use the UI APK (com.example.xspoof.apk) to configure settings
3. Reboot your device
4. All configured spoofing will be active

## Configuration

Use the companion UI APK to:
- Enable/disable spoofing globally or per app
- Set device manufacturer, model, fingerprint
- Configure Android ID and network spoofing
- Select target apps

## Files

- `module.prop` - Module metadata
- `common/system.prop` - System property overrides
- `post-fs-data.sh` - Post-FS initialization
- `service.sh` - Background service
