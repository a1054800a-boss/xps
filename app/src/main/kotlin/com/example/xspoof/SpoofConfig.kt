package com.example.xspoof

import android.content.Context
import com.google.gson.Gson

data class SpoofConfig(
    val enabled: Boolean = true,
    val spoofAll: Boolean = false,
    val targetApps: List<String> = emptyList(),
    val manufacturer: String = "Google",
    val model: String = "Pixel 6",
    val device: String = "oriole",
    val product: String = "oriole",
    val brand: String = "google",
    val hardware: String = "oriole",
    val fingerprint: String = "google/oriole/oriole:12/S2B2.220816.016:user/release-keys",
    val display: String = "S2B2.220816.016",
    val androidId: String = "a1b2c3d4e5f6g7h8",
    val ipv4: String = "192.0.2.123",
    val ipv6: String = "2001:db8::1"
) {
    fun shouldSpoofPackage(packageName: String): Boolean {
        return spoofAll || packageName in targetApps
    }

    companion object {
        private const val PREFS_NAME = "xspoof_config"
        private const val KEY_CONFIG = "config_json"
        private val gson = Gson()

        fun loadFromPrefs(context: Context): SpoofConfig {
            return try {
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val json = prefs.getString(KEY_CONFIG, null)
                if (json != null) {
                    gson.fromJson(json, SpoofConfig::class.java)
                } else {
                    SpoofConfig()
                }
            } catch (e: Exception) {
                SpoofConfig()
            }
        }

        fun saveToPrefs(context: Context, config: SpoofConfig) {
            try {
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val json = gson.toJson(config)
                prefs.edit().putString(KEY_CONFIG, json).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
