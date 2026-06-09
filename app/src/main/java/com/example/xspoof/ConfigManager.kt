package com.example.xspoof

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.File

/**
 * Manages configuration file read/write for APK + Magisk module combo
 * Writes to /data/adb/modules/xspoof/config.json for Magisk module to read
 */
class ConfigManager(private val context: Context) {
    companion object {
        private const val TAG = "XSpoof:ConfigManager"
        // Magisk module directory
        private const val MAGISK_MODULE_PATH = "/data/adb/modules/xspoof"
        private const val CONFIG_FILE_NAME = "config.json"
        private const val FALLBACK_PREFS = "xspoof_config"
        private const val FALLBACK_KEY = "config_json"
        private val gson = Gson()
    }

    /**
     * Load configuration from Magisk module path or fallback to SharedPreferences
     */
    fun loadConfig(): SpoofConfig {
        return try {
            val moduleConfigFile = File(MAGISK_MODULE_PATH, CONFIG_FILE_NAME)
            
            if (moduleConfigFile.exists() && moduleConfigFile.canRead()) {
                Log.d(TAG, "Loading config from Magisk module: ${moduleConfigFile.absolutePath}")
                val jsonString = moduleConfigFile.readText()
                gson.fromJson(jsonString, SpoofConfig::class.java) ?: SpoofConfig()
            } else {
                Log.d(TAG, "Module config not accessible, falling back to SharedPreferences")
                loadFromSharedPreferences()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading config: ${e.message}", e)
            loadFromSharedPreferences()
        }
    }

    /**
     * Save configuration to Magisk module path and SharedPreferences as backup
     */
    fun saveConfig(config: SpoofConfig): Boolean {
        var success = false

        // Try to save to Magisk module path (primary location)
        try {
            val moduleDir = File(MAGISK_MODULE_PATH)
            if (moduleDir.exists() && moduleDir.canWrite()) {
                val configFile = File(moduleDir, CONFIG_FILE_NAME)
                val jsonString = gson.toJson(config)
                configFile.writeText(jsonString)
                Log.d(TAG, "Config saved to Magisk module: ${configFile.absolutePath}")
                success = true
            } else {
                Log.w(TAG, "Magisk module directory not writable: $MAGISK_MODULE_PATH")
                Log.w(TAG, "Ensure app has required permissions and Magisk module is installed")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving to Magisk module: ${e.message}", e)
        }

        // Also save to SharedPreferences as fallback
        try {
            saveToSharedPreferences(config)
            Log.d(TAG, "Config saved to SharedPreferences (fallback)")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving to SharedPreferences: ${e.message}", e)
        }

        return success
    }

    /**
     * Fallback: Load from SharedPreferences
     */
    private fun loadFromSharedPreferences(): SpoofConfig {
        return try {
            val prefs = context.getSharedPreferences(FALLBACK_PREFS, Context.MODE_PRIVATE)
            val json = prefs.getString(FALLBACK_KEY, null)
            if (json != null) {
                gson.fromJson(json, SpoofConfig::class.java) ?: SpoofConfig()
            } else {
                SpoofConfig()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading from SharedPreferences: ${e.message}", e)
            SpoofConfig()
        }
    }

    /**
     * Fallback: Save to SharedPreferences
     */
    private fun saveToSharedPreferences(config: SpoofConfig) {
        val prefs = context.getSharedPreferences(FALLBACK_PREFS, Context.MODE_PRIVATE)
        val json = gson.toJson(config)
        prefs.edit().putString(FALLBACK_KEY, json).apply()
    }

    /**
     * Check if Magisk module is installed and accessible
     */
    fun isMagiskModuleAccessible(): Boolean {
        return try {
            val moduleDir = File(MAGISK_MODULE_PATH)
            moduleDir.exists() && moduleDir.canRead()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get module installation status
     */
    fun getModuleStatus(): ModuleStatus {
        return try {
            val moduleDir = File(MAGISK_MODULE_PATH)
            val modulePropsFile = File(moduleDir, "module.prop")
            val configFile = File(moduleDir, CONFIG_FILE_NAME)

            when {
                !moduleDir.exists() -> ModuleStatus.NOT_INSTALLED
                !modulePropsFile.exists() -> ModuleStatus.PARTIALLY_INSTALLED
                !configFile.exists() -> ModuleStatus.INSTALLED_NO_CONFIG
                moduleDir.canWrite() && configFile.canWrite() -> ModuleStatus.READY
                moduleDir.canRead() && configFile.canRead() -> ModuleStatus.INSTALLED_READ_ONLY
                else -> ModuleStatus.INSTALLED_INACCESSIBLE
            }
        } catch (e: Exception) {
            ModuleStatus.ERROR
        }
    }

    enum class ModuleStatus {
        NOT_INSTALLED,
        PARTIALLY_INSTALLED,
        INSTALLED_NO_CONFIG,
        INSTALLED_READ_ONLY,
        INSTALLED_INACCESSIBLE,
        READY,
        ERROR
    }
}
