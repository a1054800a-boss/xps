package com.example.xspoof.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.xspoof.SpoofConfig
import com.example.xspoof.ui.theme.XSpoofTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XSpoofTheme {
                XSpoofApp(context = this)
            }
        }
    }
}

@Composable
fun XSpoofApp(context: MainActivity) {
    var config by remember { mutableStateOf(SpoofConfig.load(context)) }
    var currentTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("XSpoof - Device Spoofer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Navigation
            TabRow(selectedTabIndex = currentTab) {
                Tab(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    text = { Text("General") }
                )
                Tab(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    text = { Text("Device Info") }
                )
                Tab(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    text = { Text("Network") }
                )
                Tab(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 },
                    text = { Text("Target Apps") }
                )
            }

            // Tab Content
            when (currentTab) {
                0 -> GeneralTab(config) { newConfig ->
                    config = newConfig
                    SpoofConfig.save(context, newConfig)
                }
                1 -> DeviceInfoTab(config) { newConfig ->
                    config = newConfig
                    SpoofConfig.save(context, newConfig)
                }
                2 -> NetworkTab(config) { newConfig ->
                    config = newConfig
                    SpoofConfig.save(context, newConfig)
                }
                3 -> TargetAppsTab(config) { newConfig ->
                    config = newConfig
                    SpoofConfig.save(context, newConfig)
                }
            }
        }
    }
}

@Composable
fun GeneralTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Module Enabled",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = config.enabled,
                            onCheckedChange = { newValue ->
                                onConfigChange(config.copy(enabled = newValue))
                            }
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Spoof All Apps",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = config.spoofAll,
                            onCheckedChange = { newValue ->
                                onConfigChange(config.copy(spoofAll = newValue))
                            }
                        )
                    }
                    Text(
                        "Apply spoofing to all installed apps (not recommended)",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceInfoTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    var manufacturerField by remember { mutableStateOf(TextFieldValue(config.manufacturer)) }
    var modelField by remember { mutableStateOf(TextFieldValue(config.model)) }
    var fingerprintField by remember { mutableStateOf(TextFieldValue(config.fingerprint)) }
    var androidIdField by remember { mutableStateOf(TextFieldValue(config.androidId)) }
    var deviceField by remember { mutableStateOf(TextFieldValue(config.device)) }
    var productField by remember { mutableStateOf(TextFieldValue(config.product)) }
    var brandField by remember { mutableStateOf(TextFieldValue(config.brand)) }
    var hardwareField by remember { mutableStateOf(TextFieldValue(config.hardware)) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = manufacturerField,
                onValueChange = { manufacturerField = it },
                label = { Text("Manufacturer") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { newValue ->
                    manufacturerField = newValue
                    onConfigChange(config.copy(manufacturer = newValue.text))
                }
            )
        }
        item {
            OutlinedTextField(
                value = modelField,
                onValueChange = { modelField = it },
                label = { Text("Model") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { newValue ->
                    modelField = newValue
                    onConfigChange(config.copy(model = newValue.text))
                }
            )
        }
        item {
            OutlinedTextField(
                value = fingerprintField,
                onValueChange = { fingerprintField = it },
                label = { Text("Fingerprint") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 2,
                onValueChange = { newValue ->
                    fingerprintField = newValue
                    onConfigChange(config.copy(fingerprint = newValue.text))
                }
            )
        }
        item {
            OutlinedTextField(
                value = androidIdField,
                onValueChange = { androidIdField = it },
                label = { Text("Android ID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { newValue ->
                    androidIdField = newValue
                    onConfigChange(config.copy(androidId = newValue.text))
                }
            )
        }
        item {
            OutlinedTextField(
                value = deviceField,
                onValueChange = { deviceField = it },
                label = { Text("Device") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { newValue ->
                    deviceField = newValue
                    onConfigChange(config.copy(device = newValue.text))
                }
            )
        }
        item {
            OutlinedTextField(
                value = productField,
                onValueChange = { productField = it },
                label = { Text("Product") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { newValue ->
                    productField = newValue
                    onConfigChange(config.copy(product = newValue.text))
                }
            )
        }
        item {
            OutlinedTextField(
                value = brandField,
                onValueChange = { brandField = it },
                label = { Text("Brand") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { newValue ->
                    brandField = newValue
                    onConfigChange(config.copy(brand = newValue.text))
                }
            )
        }
        item {
            OutlinedTextField(
                value = hardwareField,
                onValueChange = { hardwareField = it },
                label = { Text("Hardware") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { newValue ->
                    hardwareField = newValue
                    onConfigChange(config.copy(hardware = newValue.text))
                }
            )
        }
    }
}

@Composable
fun NetworkTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    var ipv4Field by remember { mutableStateOf(TextFieldValue(config.ipv4)) }
    var ipv6Field by remember { mutableStateOf(TextFieldValue(config.ipv6)) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = ipv4Field,
                onValueChange = { newValue ->
                    ipv4Field = newValue
                    onConfigChange(config.copy(ipv4 = newValue.text))
                },
                label = { Text("IPv4 Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("192.0.2.123") }
            )
        }
        item {
            OutlinedTextField(
                value = ipv6Field,
                onValueChange = { newValue ->
                    ipv6Field = newValue
                    onConfigChange(config.copy(ipv6 = newValue.text))
                },
                label = { Text("IPv6 Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("2001:db8::1") }
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Network Spoofing",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Configure fake IPv4 and IPv6 addresses for network requests",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TargetAppsTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    var showAddDialog by remember { mutableStateOf(false) }
    var appNameInput by remember { mutableStateOf(TextFieldValue("")) }
    var targetApps by remember { mutableStateOf(config.targetApps.toMutableList()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add App",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Add Target App")
            }
        }

        items(targetApps.size) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        targetApps[index],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            targetApps.removeAt(index)
                            onConfigChange(config.copy(targetApps = targetApps.toList()))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Target App") },
            text = {
                OutlinedTextField(
                    value = appNameInput,
                    onValueChange = { appNameInput = it },
                    label = { Text("Package Name") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("com.example.app") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (appNameInput.text.isNotEmpty()) {
                            targetApps.add(appNameInput.text)
                            onConfigChange(config.copy(targetApps = targetApps.toList()))
                            appNameInput = TextFieldValue("")
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
