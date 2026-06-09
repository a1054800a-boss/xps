package com.example.xspoof.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xspoof.SpoofConfig
import com.example.xspoof.ui.theme.XSpoofTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XSpoofTheme {
                XSpoofApp()
            }
        }
    }
}

@Composable
fun XSpoofApp() {
    var config by remember { mutableStateOf(SpoofConfig()) }
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "XSpoof",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Device Information Spoofer",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("General") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Device") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Network") }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text("Apps") }
            )
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> GeneralTab(config) { config = it }
                1 -> DeviceTab(config) { config = it }
                2 -> NetworkTab(config) { config = it }
                3 -> AppsTab(config) { config = it }
            }
        }
    }
}

@Composable
fun GeneralTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SwitchCard("Module Enabled", config.enabled) {
            onConfigChange(config.copy(enabled = it))
        }
        SwitchCard("Spoof All Apps", config.spoofAll) {
            onConfigChange(config.copy(spoofAll = it))
        }
        Text(
            "Enable to spoof device info for all installed apps. Disable to only spoof selected apps.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun DeviceTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextFieldCard("Manufacturer", config.manufacturer) {
            onConfigChange(config.copy(manufacturer = it))
        }
        TextFieldCard("Model", config.model) {
            onConfigChange(config.copy(model = it))
        }
        TextFieldCard("Device", config.device) {
            onConfigChange(config.copy(device = it))
        }
        TextFieldCard("Product", config.product) {
            onConfigChange(config.copy(product = it))
        }
        TextFieldCard("Brand", config.brand) {
            onConfigChange(config.copy(brand = it))
        }
        TextFieldCard("Hardware", config.hardware) {
            onConfigChange(config.copy(hardware = it))
        }
        TextFieldCard("Fingerprint", config.fingerprint) {
            onConfigChange(config.copy(fingerprint = it))
        }
        TextFieldCard("Android ID", config.androidId) {
            onConfigChange(config.copy(androidId = it))
        }
    }
}

@Composable
fun NetworkTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextFieldCard("IPv4 Address", config.ipv4) {
            onConfigChange(config.copy(ipv4 = it))
        }
        TextFieldCard("IPv6 Address", config.ipv6) {
            onConfigChange(config.copy(ipv6 = it))
        }
    }
}

@Composable
fun AppsTab(config: SpoofConfig, onConfigChange: (SpoofConfig) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Selected Apps to Spoof:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        if (config.targetApps.isEmpty()) {
            Text(
                "No apps selected. Enable 'Spoof All Apps' in General tab to spoof all apps.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            config.targetApps.forEach { app ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        app,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchCard(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun TextFieldCard(label: String, value: String, onValueChange: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}
