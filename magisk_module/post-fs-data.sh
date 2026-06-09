#!/system/bin/sh
# XSpoof Magisk Module - Post FS Data Script
MODDIR=${0%/*}

# Create module directories
mkdir -p "$MODDIR/data"
mkdir -p "$MODDIR/common"

# Set permissions
chmod 755 "$MODDIR/data"
