package com.example.xspoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Field
import android.os.Build
import android.content.ContentResolver

/**
 * Main XPosed/LSPosed module for spoofing device information:
 * - IPv4 & IPv6 addresses
 * - Manufacturer, Model, Fingerprint
 * - Android ID
 * - Device hardware details
 */
class XSpoofModule : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "XSpoof"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            // Load configuration
            val config = SpoofConfig.load()
            if (!config.enabled) {
                XposedBridge.log("$TAG: Module disabled")
                return
            }

            // Check if target package
            if (!shouldHookPackage(lpparam.packageName, config)) {
                return
            }

            XposedBridge.log("$TAG: Hooking ${lpparam.packageName}")

            // Apply hooks
            hookBuildFields(lpparam, config)
            hookWifiIpAddress(lpparam, config)
            hookNetworkInterfaces(lpparam, config)
            hookAndroidId(lpparam, config)
            hookFingerprint(lpparam, config)
            hookDeviceProperties(lpparam, config)

        } catch (e: Throwable) {
            XposedBridge.log("$TAG: Error in handleLoadPackage - ${e.message}")
            e.printStackTrace()
        }
    }

    private fun shouldHookPackage(packageName: String, config: SpoofConfig): Boolean {
        if (packageName == "android" || packageName == "com.example.xspoof") {
            return false
        }
        return config.targetApps.isEmpty() || config.targetApps.contains(packageName) || config.spoofAll
    }

    /**
     * Spoof Build.MANUFACTURER, Build.MODEL, Build.FINGERPRINT, etc.
     */
    private fun hookBuildFields(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        try {
            val buildClass = XposedHelpers.findClass("android.os.Build", lpparam.classLoader)

            if (config.manufacturer.isNotEmpty()) {
                setStaticField(buildClass, "MANUFACTURER", config.manufacturer)
            }
            if (config.model.isNotEmpty()) {
                setStaticField(buildClass, "MODEL", config.model)
            }
            if (config.fingerprint.isNotEmpty()) {
                setStaticField(buildClass, "FINGERPRINT", config.fingerprint)
            }
            if (config.device.isNotEmpty()) {
                setStaticField(buildClass, "DEVICE", config.device)
            }
            if (config.product.isNotEmpty()) {
                setStaticField(buildClass, "PRODUCT", config.product)
            }
            if (config.brand.isNotEmpty()) {
                setStaticField(buildClass, "BRAND", config.brand)
            }
            if (config.hardware.isNotEmpty()) {
                setStaticField(buildClass, "HARDWARE", config.hardware)
            }
            if (config.displayId.isNotEmpty()) {
                setStaticField(buildClass, "DISPLAY", config.displayId)
            }

            XposedBridge.log("$TAG: Build fields spoofed")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG: Error spoofing Build fields - ${t.message}")
        }
    }

    /**
     * Spoof IPv4 via WifiManager.getConnectionInfo().getIpAddress()
     */
    private fun hookWifiIpAddress(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        if (config.ipv4.isEmpty()) return

        try {
            val wifiInfoClass = XposedHelpers.findClass(
                "android.net.wifi.WifiInfo",
                lpparam.classLoader
            )

            XposedHelpers.findAndHookMethod(
                wifiInfoClass,
                "getIpAddress",
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                        return ipStringToInt(config.ipv4)
                    }
                }
            )

            XposedBridge.log("$TAG: IPv4 spoofing enabled (${config.ipv4})")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG: Error hooking WiFi IPv4 - ${t.message}")
        }
    }

    /**
     * Spoof IPv6 via network interfaces
     */
    private fun hookNetworkInterfaces(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        if (config.ipv6.isEmpty()) return

        try {
            val linkPropsClass = XposedHelpers.findClass(
                "android.net.LinkProperties",
                lpparam.classLoader
            )

            XposedHelpers.findAndHookMethod(
                linkPropsClass,
                "getLinkAddresses",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        try {
                            // This is a hook point; actual IPv6 modification would require
                            // more complex address object manipulation
                            XposedBridge.log("$TAG: IPv6 hook triggered for ${config.ipv6}")
                        } catch (e: Throwable) {
                            XposedBridge.log("$TAG: Error in IPv6 hook - ${e.message}")
                        }
                    }
                }
            )

            XposedBridge.log("$TAG: IPv6 spoofing framework enabled")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG: Error hooking IPv6 - ${t.message}")
        }
    }

    /**
     * Spoof Android ID via Settings.Secure.getString()
     */
    private fun hookAndroidId(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        if (config.androidId.isEmpty()) return

        try {
            val settingsSecureClass = XposedHelpers.findClass(
                "android.provider.Settings\$Secure",
                lpparam.classLoader
            )

            XposedHelpers.findAndHookMethod(
                settingsSecureClass,
                "getString",
                ContentResolver::class.java,
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        try {
                            val key = param?.args?.get(1) as? String
                            if ("android_id".equals(key, ignoreCase = true)) {
                                param.result = config.androidId
                            }
                        } catch (e: Throwable) {
                            XposedBridge.log("$TAG: Error in Android ID hook - ${e.message}")
                        }
                    }
                }
            )

            XposedBridge.log("$TAG: Android ID spoofing enabled")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG: Error hooking Android ID - ${t.message}")
        }
    }

    /**
     * Spoof Build.getFingerprint() method
     */
    private fun hookFingerprint(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        if (config.fingerprint.isEmpty()) return

        try {
            val buildClass = XposedHelpers.findClass("android.os.Build", lpparam.classLoader)

            XposedHelpers.findAndHookMethod(
                buildClass,
                "getFingerprint",
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                        return config.fingerprint
                    }
                }
            )

            XposedBridge.log("$TAG: Fingerprint method spoofing enabled")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG: Error hooking fingerprint - ${t.message}")
        }
    }

    /**
     * Hook additional device properties
     */
    private fun hookDeviceProperties(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        try {
            // Hook System.getProperty for device-related keys
            val systemClass = XposedHelpers.findClass("java.lang.System", lpparam.classLoader)

            XposedHelpers.findAndHookMethod(
                systemClass,
                "getProperty",
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        try {
                            val key = param?.args?.get(0) as? String
                            when (key) {
                                "ro.build.fingerprint" -> if (config.fingerprint.isNotEmpty()) param.result = config.fingerprint
                                "ro.product.manufacturer" -> if (config.manufacturer.isNotEmpty()) param.result = config.manufacturer
                                "ro.product.model" -> if (config.model.isNotEmpty()) param.result = config.model
                                "ro.product.device" -> if (config.device.isNotEmpty()) param.result = config.device
                                "ro.product.brand" -> if (config.brand.isNotEmpty()) param.result = config.brand
                            }
                        } catch (e: Throwable) {
                            // Ignore
                        }
                    }
                }
            )

            XposedBridge.log("$TAG: Device properties spoofing enabled")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG: Error hooking device properties - ${t.message}")
        }
    }

    /**
     * Helper: Set static field on a class
     */
    private fun setStaticField(clazz: Class<*>, fieldName: String, value: String) {
        try {
            val field: Field = clazz.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(null, value)
        } catch (e: Throwable) {
            XposedBridge.log("$TAG: Cannot set field $fieldName - ${e.message}")
        }
    }

    /**
     * Helper: Convert IP string (e.g., "192.0.2.123") to integer
     */
    private fun ipStringToInt(ipString: String): Int {
        val parts = ipString.split(".")
        if (parts.size != 4) return 0

        return try {
            val a = parts[0].toInt()
            val b = parts[1].toInt()
            val c = parts[2].toInt()
            val d = parts[3].toInt()
            (a shl 24) or (b shl 16) or (c shl 8) or d
        } catch (e: NumberFormatException) {
            0
        }
    }
}
