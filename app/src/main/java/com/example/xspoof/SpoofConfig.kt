package com.example.xspoof

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Configuration data class for device spoofing settings
 */
data class SpoofConfig(
    @SerializedName("enabled")
    val enabled: Boolean = true,

    @SerializedName("spoof_all")
    val spoofAll: Boolean = false,

    @SerializedName("target_apps")
    val targetApps: List<String> = emptyList(),

    @SerializedName("manufacturer")
    val manufacturer: String = "Google",

    @SerializedName("model")
    val model: String = "Pixel 6",

    @SerializedName("fingerprint")
    val fingerprint: String = "google/oriole/oriole:12/S2B2.220816.016:user/release-keys",

    @SerializedName("device")
    val device: String = "oriole",

    @SerializedName("product")
    val product: String = "oriole",

    @SerializedName("brand")
    val brand: String = "google",

    @SerializedName("hardware")
    val hardware: String = "oriole",

    @SerializedName("display_id")
    val displayId: String = "S2B2.220816.016.user",

    @SerializedName("android_id")
    val androidId: String = "a1b2c3d4e5f6g7h8",

    @SerializedName("ipv4")
    val ipv4: String = "192.0.2.123",

    @SerializedName("ipv6")
    val ipv6: String = "2001:db8::1"
) {
    companion object {
        private const val PREFS_NAME = "xspoof_config"
        private const val CONFIG_KEY = "config_json"
        private val gson = Gson()

        fun load(context: Context? = null): SpoofConfig {
            return try {
                if (context == null) return SpoofConfig()
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_WORLD_READABLE)
                val json = prefs.getString(CONFIG_KEY, null) ?: return SpoofConfig()
                gson.fromJson(json, SpoofConfig::class.java)
            } catch (e: Exception) {
                SpoofConfig()
            }
        }

        fun save(context: Context, config: SpoofConfig) {
            try {
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_WORLD_READABLE)
                val json = gson.toJson(config)
                prefs.edit().putString(CONFIG_KEY, json).apply()
            } catch (e: Exception) {
                // Log error
            }
        }
    }
}
