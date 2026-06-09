package com.example.xspoof.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.xspoof.ConfigManager
import com.example.xspoof.SpoofConfig

/**
 * Settings UI component for XSpoof APK
 * Displays current configuration status and allows modifications
 */
@Composable
fun SettingsScreen(
    configManager: ConfigManager,
    onConfigSaved: (SpoofConfig) -> Unit
) {
    var config by remember { mutableStateOf(configManager.loadConfig()) }
    var moduleStatus by remember { mutableStateOf(configManager.getModuleStatus()) }
    var showStatusAlert by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        moduleStatus = configManager.getModuleStatus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Module Status Card
        ModuleStatusCard(moduleStatus)

        Spacer(modifier = Modifier.height(16.dp))

        // General Settings
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "General Settings",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Enable Module")
                    Switch(
                        checked = config.enabled,
                        onCheckedChange = {
                            config = config.copy(enabled = it)
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Spoof All Apps")
                    Switch(
                        checked = config.spoofAll,
                        onCheckedChange = {
                            config = config.copy(spoofAll = it)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Device Information
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Device Information",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = config.manufacturer,
                    onValueChange = { config = config.copy(manufacturer = it) },
                    label = { Text("Manufacturer") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                TextField(
                    value = config.model,
                    onValueChange = { config = config.copy(model = it) },
                    label = { Text("Model") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                TextField(
                    value = config.fingerprint,
                    onValueChange = { config = config.copy(fingerprint = it) },
                    label = { Text("Fingerprint") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                TextField(
                    value = config.device,
                    onValueChange = { config = config.copy(device = it) },
                    label = { Text("Device") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                TextField(
                    value = config.brand,
                    onValueChange = { config = config.copy(brand = it) },
                    label = { Text("Brand") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                TextField(
                    value = config.hardware,
                    onValueChange = { config = config.copy(hardware = it) },
                    label = { Text("Hardware") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                TextField(
                    value = config.androidId,
                    onValueChange = { config = config.copy(androidId = it) },
                    label = { Text("Android ID") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (configManager.saveConfig(config)) {
                        onConfigSaved(config)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }

            Button(
                onClick = {
                    config = configManager.loadConfig()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reload")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Note: Changes require device reboot to take effect. " +
                    "Ensure Magisk module is installed at /data/adb/modules/xspoof/",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun ModuleStatusCard(status: ConfigManager.ModuleStatus) {
    val statusText = when (status) {
        ConfigManager.ModuleStatus.NOT_INSTALLED -> "Magisk module not installed"
        ConfigManager.ModuleStatus.PARTIALLY_INSTALLED -> "Magisk module partially installed"
        ConfigManager.ModuleStatus.INSTALLED_NO_CONFIG -> "Module installed, no config yet"
        ConfigManager.ModuleStatus.INSTALLED_READ_ONLY -> "Module read-only, limited functionality"
        ConfigManager.ModuleStatus.INSTALLED_INACCESSIBLE -> "Module inaccessible"
        ConfigManager.ModuleStatus.READY -> "Module ready and operational"
        ConfigManager.ModuleStatus.ERROR -> "Error checking module status"
    }

    val statusColor = when (status) {
        ConfigManager.ModuleStatus.READY -> MaterialTheme.colorScheme.primary
        ConfigManager.ModuleStatus.NOT_INSTALLED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.warning
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Module Status",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                statusText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
