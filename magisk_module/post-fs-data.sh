#!/system/bin/sh
# XSpoof Post-FS-Data Hook
# Loads and applies device spoofing configuration

MODDIR=${0%/*}
LOG_FILE="$MODDIR/xspoof.log"
CONFIG_FILE="$MODDIR/config.json"
PROP_OVERRIDE="$MODDIR/system.prop.override"

log_to_file() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_FILE"
}

log_to_file "XSpoof: post-fs-data.sh executed"

# Wait for system to stabilize
sleep 2

# Check if config exists
if [ ! -f "$CONFIG_FILE" ]; then
    log_to_file "Config file not found, using defaults"
    exit 0
fi

# Parse JSON config and apply properties
log_to_file "Parsing configuration from $CONFIG_FILE"

# Extract values from JSON using grep and sed (basic parsing)
if command -v jq &> /dev/null; then
    # If jq is available, use it for proper JSON parsing
    ENABLED=$(jq -r '.enabled // false' "$CONFIG_FILE" 2>/dev/null)
    MANUFACTURER=$(jq -r '.manufacturer // "Google"' "$CONFIG_FILE" 2>/dev/null)
    MODEL=$(jq -r '.model // "Pixel 6"' "$CONFIG_FILE" 2>/dev/null)
    FINGERPRINT=$(jq -r '.fingerprint // "google/oriole/oriole:12/S2B2.220816.016:user/release-keys"' "$CONFIG_FILE" 2>/dev/null)
    DEVICE=$(jq -r '.device // "oriole"' "$CONFIG_FILE" 2>/dev/null)
    PRODUCT=$(jq -r '.product // "oriole"' "$CONFIG_FILE" 2>/dev/null)
    BRAND=$(jq -r '.brand // "google"' "$CONFIG_FILE" 2>/dev/null)
    HARDWARE=$(jq -r '.hardware // "oriole"' "$CONFIG_FILE" 2>/dev/null)
    ANDROID_ID=$(jq -r '.android_id // "a1b2c3d4e5f6g7h8"' "$CONFIG_FILE" 2>/dev/null)
else
    # Fallback: basic grep parsing (less reliable)
    ENABLED=$(grep -o '"enabled":[^,}]*' "$CONFIG_FILE" | grep -o 'true\|false')
    MANUFACTURER=$(grep -o '"manufacturer":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
    MODEL=$(grep -o '"model":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
    FINGERPRINT=$(grep -o '"fingerprint":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
    DEVICE=$(grep -o '"device":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
    PRODUCT=$(grep -o '"product":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
    BRAND=$(grep -o '"brand":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
    HARDWARE=$(grep -o '"hardware":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
    ANDROID_ID=$(grep -o '"android_id":"[^"]*"' "$CONFIG_FILE" | cut -d'"' -f4)
fi

if [ "$ENABLED" != "true" ]; then
    log_to_file "Module disabled in config"
    exit 0
fi

log_to_file "Loaded config - Manufacturer: $MANUFACTURER, Model: $MODEL"

# Create property override file
cat > "$PROP_OVERRIDE" << EOF
ro.build.fingerprint=$FINGERPRINT
ro.product.manufacturer=$MANUFACTURER
ro.product.model=$MODEL
ro.product.device=$DEVICE
ro.product.brand=$BRAND
ro.product.hardware=$HARDWARE
ro.build.product=$PRODUCT
EOF

log_to_file "Property overrides written to $PROP_OVERRIDE"
log_to_file "XSpoof initialized successfully"
