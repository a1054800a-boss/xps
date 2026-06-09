package com.example.xspoof

import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import android.os.Build
import android.provider.Settings

class XSpoofModule : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "XSpoof"
        private const val CONFIG_PREFS = "xspoof_config"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) return

        try {
            val config = SpoofConfig.loadFromPrefs(lpparam.appContext)

            if (!config.enabled) return

            // Check if we should spoof this package
            if (!config.shouldSpoofPackage(lpparam.packageName)) return

            XposedBridge.log("XSpoof: Hooking ${lpparam.packageName}")

            // Hook Build class static fields
            hookBuildFields(lpparam, config)

            // Hook device properties
            hookDeviceProperties(lpparam, config)

            // Hook Android ID
            hookAndroidId(lpparam, config)

            // Hook network information
            hookNetworkInfo(lpparam, config)

        } catch (e: Exception) {
            XposedBridge.log("XSpoof: Error - ${e.message}")
            e.printStackTrace()
        }
    }

    private fun hookBuildFields(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        val buildClass = XposedHelpers.findClass("android.os.Build", lpparam.classLoader)

        XposedHelpers.setStaticObjectField(buildClass, "MANUFACTURER", config.manufacturer)
        XposedHelpers.setStaticObjectField(buildClass, "MODEL", config.model)
        XposedHelpers.setStaticObjectField(buildClass, "DEVICE", config.device)
        XposedHelpers.setStaticObjectField(buildClass, "PRODUCT", config.product)
        XposedHelpers.setStaticObjectField(buildClass, "BRAND", config.brand)
        XposedHelpers.setStaticObjectField(buildClass, "HARDWARE", config.hardware)
        XposedHelpers.setStaticObjectField(buildClass, "FINGERPRINT", config.fingerprint)
        XposedHelpers.setStaticObjectField(buildClass, "DISPLAY", config.display)

        XposedBridge.log("XSpoof: Build fields spoofed to ${config.manufacturer} ${config.model}")
    }

    private fun hookDeviceProperties(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        try {
            val systemClass = XposedHelpers.findClass("java.lang.System", lpparam.classLoader)
            val method = XposedHelpers.findMethodExact(systemClass, "getProperty", String::class.java)

            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    param.result = when (key) {
                        "ro.product.manufacturer" -> config.manufacturer
                        "ro.product.model" -> config.model
                        "ro.product.device" -> config.device
                        "ro.product.product" -> config.product
                        "ro.product.brand" -> config.brand
                        "ro.hardware" -> config.hardware
                        "ro.build.fingerprint" -> config.fingerprint
                        "ro.build.display.id" -> config.display
                        else -> param.result
                    }
                }
            })
            XposedBridge.log("XSpoof: System.getProperty() hooked")
        } catch (e: Exception) {
            XposedBridge.log("XSpoof: Failed to hook System.getProperty() - ${e.message}")
        }
    }

    private fun hookAndroidId(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        try {
            val settingsClass = XposedHelpers.findClass("android.provider.Settings.Secure", lpparam.classLoader)
            val method = XposedHelpers.findMethodExact(settingsClass, "getString", android.content.ContentResolver::class.java, String::class.java)

            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[1] as String
                    if (key == "android_id") {
                        param.result = config.androidId
                    }
                }
            })
            XposedBridge.log("XSpoof: Android ID spoofing enabled")
        } catch (e: Exception) {
            XposedBridge.log("XSpoof: Failed to hook Android ID - ${e.message}")
        }
    }

    private fun hookNetworkInfo(lpparam: XC_LoadPackage.LoadPackageParam, config: SpoofConfig) {
        try {
            val wifiInfoClass = XposedHelpers.findClass("android.net.wifi.WifiInfo", lpparam.classLoader)
            val method = XposedHelpers.findMethodExact(wifiInfoClass, "getIpAddress")

            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (config.ipv4.isNotEmpty()) {
                        param.result = parseIpAddress(config.ipv4)
                    }
                }
            })
            XposedBridge.log("XSpoof: IPv4 spoofing enabled (${config.ipv4})")
        } catch (e: Exception) {
            XposedBridge.log("XSpoof: Failed to hook WifiInfo - ${e.message}")
        }
    }

    private fun parseIpAddress(ip: String): Int {
        val parts = ip.split(".")
        return if (parts.size == 4) {
            ((parts[0].toIntOrNull() ?: 0) shl 0) or
            ((parts[1].toIntOrNull() ?: 0) shl 8) or
            ((parts[2].toIntOrNull() ?: 0) shl 16) or
            ((parts[3].toIntOrNull() ?: 0) shl 24)
        } else 0
    }
}
